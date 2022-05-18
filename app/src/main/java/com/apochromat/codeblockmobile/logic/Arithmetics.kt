package com.apochromat.codeblockmobile.logic

import java.util.*
import kotlin.math.*

fun arithmetics(heap: Heap, expression: String): Pair<String, Int> {
    val exp = expression.replace("\\s".toRegex(), "")
    if (exp.isEmpty()) {
        return Pair("Empty input", 0)
    }
    val (prepared, expStatus) = preparingExpression(heap, exp)
    val (correctLine, lineStatus) = lineCheck(exp)
    if (lineStatus == 0) {
        return Pair(correctLine, 0)
    }
    if (expStatus == 0) {
        return Pair(prepared, 0)
    }
    return rpnToAnswer(expressionToRPN(prepared))
}

fun getPriority(token: Char): Int {
    return when (token) {
        '#','^' -> 4
        '*', '/', '%' -> 3
        '+', '-' -> 2
        '(' -> 1
        ')' -> -1
        else -> 0
    }
}

fun expressionToRPN(expression: String): String {
    var current = ""
    val stack: Stack<Char> = Stack<Char>()
    var priority: Int
    for (i in expression.indices) {
        priority = getPriority(expression[i])
        when (priority) {
            0 -> current += expression[i]
            1 -> stack.push(expression[i])
            2, 3,4 -> {
                current += " "
                while (!stack.empty()) {
                    if ((getPriority(stack.peek()) >= priority)) current += stack.pop()
                    else break
                }
                stack.push(expression[i])
            }
            -1 -> {
                current += " "
                while (getPriority(stack.peek()) != 1) current += stack.pop()
                stack.pop()
            }
            else -> {
                return "Error"
            }
        }
    }
    while (!stack.empty()) current += stack.pop()
    return current
}

fun rpnToAnswer(rpn: String): Pair<String, Int> {
    var operand = String()
    val stack: Stack<Int> = Stack<Int>()
    var i = 0
    while (i < rpn.length) {
        if (rpn[i] == ' ') {
            i++
            continue
        }
        if (getPriority(rpn[i]) == 0) {
            while (rpn[i] != ' ' && (getPriority(rpn[i]) == 0)) {
                operand += rpn[i++]
                if (i == rpn.length) break
            }
            try {
                stack.push(operand.toInt())
            } catch (e: NumberFormatException) {
                return Pair("Unexpected symbol $operand", 0)
            }
            operand = String()
        }
        if (i == rpn.length) break
        if (getPriority(rpn[i]) > 1) {
            try {
                val a: Int = stack.pop()
                val b: Int = stack.pop()
                when (rpn[i]) {
                    '^' ->{
                        val result = b.toDouble().pow(a).toLong()
                        if(result>=2147483647){
                            return Pair("Memory limit", 0)
                        }
                        stack.push(result.toInt())
                    }
                    '#' ->{
                        val step =1/a.toDouble()
                        val result = b.toDouble().pow(step).toLong()

                        if (b<0){
                            return Pair("Root of a negative number",0)
                        }
                        if(result>=2147483647){
                            return Pair("Memory limit", 0)
                        }
                        stack.push(result.toInt())
                    }
                    '+' -> stack.push(b + a)
                    '-' -> stack.push(b - a)
                    '*' -> stack.push(b * a)
                    '/' -> {
                        try {
                            stack.push(b / a)
                        } catch (e: Exception) {
                            return Pair("Division by zero", 0)
                        }
                    }
                    '%' ->{
                        try {
                            stack.push(b % a)
                        } catch (e: Exception){
                            return Pair("Division by zero", 0)
                        }
                    }
                    else -> {
                        return Pair("Unexpected symbol ${rpn[i]}", 0)
                    }
                }
            } catch (e: EmptyStackException) {
                return Pair("Incorrect expression", 0)
            }
        }
        i++
    }
    return Pair("OK", stack.pop())
}

fun lineCheck(string: String): Pair<String, Int> {
    var str = string.replace("[A-Za-z-+*/0-9()%_^#\\[\\]]".toRegex(), "")
    if (str.isNotEmpty()) {
        return Pair("Unexpected Symbol", 0)
    }
    val reg = "([-+%#^ ]+[0-9_]+[A_Za-z_]+[0-9]*[-+%*#^ ]*)|(\\b[0-9_]+[A-Za-z_]+[0-9]*)|(\\b[_][0-9]+)".toRegex()
    if(reg.find(string)!=null){
        return Pair("Incorrect Expression",0)
    }
    str=string.replace("[A-Za-z-+*/0-9%^#_\\[\\]]".toRegex(), "")
    val scob1=str.replace("\\(".toRegex(), "")
    val scob2 =str.replace("\\)".toRegex(), "")
    if (scob1.length!=scob2.length) {
        return Pair("Incorrect Expression", 0)
    }
    return Pair("OK", 1)
}

fun preparingExpression(heap: Heap, expression: String): Pair<String, Int> {
    var exp = expression
    var preparedExpression = String()
    val regArr="([A-Za-z]+[A-Za-z0-9_]*)\\[[A-Za-z0-9 +%*/_#^-]*]".toRegex();
    var array = regArr.find(exp);
    while (array!=null) {
        val (arrName, arrIndex) = indexCount(heap, array.value)
        if(arrIndex==-1){
            return Pair(arrName, 0)
        }
        if (!heap.isArrayExist(arrName)) {
            return Pair("Undefined array $arrName", 0)
        }
        val arrValue = heap.getArrayValue(arrName, arrIndex);
        var fromArrToNum =arrValue.toString()
        if(arrValue!!.toInt()<0){
            fromArrToNum="("+fromArrToNum+")"
        }
        exp = exp.replace(array.value, fromArrToNum)
        array = regArr.find(exp)
    }
    val reg="[A-Za-z]+[A-Za-z0-9_]*".toRegex();
    var varieble = reg.find(exp);
    while (varieble!=null) {
        val VV=varieble.value;
        if (!heap.isVariableExist(varieble.value)) {
            return Pair("Undefined varieble $VV ", 0)
        }
        val varValue=heap.getVariableValue(varieble.value)
        var fromVarToNum =varValue.toString()
        if(varValue!!.toInt()<0){
            fromVarToNum="("+fromVarToNum+")"
        }
        exp = exp.replaceRange(varieble.range, fromVarToNum)
        varieble = reg.find(exp)
    }

    for (j in exp.indices) {
        if (exp[j] == '-') {
            if (j == 0) {
                preparedExpression += "0"
            } else if (exp[j - 1] == '(') {
                preparedExpression += "0"
            }
        }
        preparedExpression += exp[j]
    }
    return Pair(preparedExpression, 1)
}
fun defineInput(heap:Heap, expression: String):Triple<String,String, Int>{
    val arr="[A-Za-z]+[\\[(\\d+_*^#)\\]]".toRegex();
    val varieble = "[A-Za-z]+[A-Za-z0-9_]*".toRegex();

    if(arr.find(expression)!=null){
        val(name, index)= indexCount(heap,expression);
        if (index==-1){
            return Triple(name, "NaN", 0);
        }
        if(heap.isArrayExist(name) && index>=0 && index<heap.getArraySize(name)!!.toInt()) {
            return Triple("Array", name, index);
        }
    }
    if(varieble.find(expression)!=null){
        if(heap.isVariableExist(expression)) {
            return Triple("Variable", expression, 0);
        }
    }
    return Triple("InputError","NaN",0);
}
fun indexCount(heap:Heap, arr:String):Pair<String,Int>{
    var array:String=arr;
    var index=-1;
    var arrname="";
    val reg="([A-Za-z]+[A-Za-z0-9_]*)\\[[A-Za-z0-9 +*/%_^#-]*]".toRegex();
    while (reg.find(array)!=null){
        val arg="\\[[A-Za-z0-9 +*/_%^#-]*]".toRegex().find(array);
        arrname="[A-Za-z]+[A-Za-z0-9_]*".toRegex().find(reg.find(array)!!.value)!!.value;
        if(arg!=null) {
            var arm=arg.value.replace("[","");
            arm=arm.replace("]","");
            var (status, rez) = arithmetics(heap, arm);
            array=array.replace(arm,rez.toString());
            index= rez;
            println(index)
            println(status)
            if(!heap.isArrayExist(arrname)){
                return Pair("Unidentified array", -1)
            }
            if (index>=heap.getArraySize(arrname)!!.toInt() || index<0){
                return Pair("Index out of range", -1)
            }
            if(status!="OK"){
                return Pair(status,-1)
            }
            val arrayValue=heap.getArrayValue(arrname,rez);
            array=array.replace(reg.find(array)!!.value, arrayValue.toString());

        }
    }
    return Pair(arrname,index);
}