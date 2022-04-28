package com.apochromat.codeblockmobile.logic

import java.util.*

fun arithmetics(heap: Heap, expression: String): Pair<String, Int> {
    val exp = expression.replace("\\s".toRegex(), "")
    if (exp.isEmpty()) {
        return Pair("Empty input", 0)
    }
    val (prepared, expStatus) = preparingExpression(heap, exp)
    val (correctLine, lineStatus) = lineCheck(exp)
    if (expStatus == 0) {
        return Pair(prepared, 0)
    }
    if (lineStatus == 0) {
        return Pair(correctLine, 0)
    }

    return rpnToAnswer(expressionToRPN(prepared))
}

fun getPriority(token: Char): Int {
    return when (token) {
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
            2, 3 -> {
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
                    '%' -> stack.push(b % a)
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
    val str = string.replace("[A-Za-z-+*/0-9()%]".toRegex(), "")
    if (str.isNotEmpty()) {
        return Pair("Unexpected Symbol", 0)
    }
    val reg = "[A-Za-z]+[0-9]|[0-9][A-Za-z]".toRegex()
    val match = reg.find(string)
    if (match != null) {
        return Pair("Incorrect expression", 0)
    }
    return Pair("OK", 1)
}

fun preparingExpression(heap: Heap, expression: String): Pair<String, Int> {
    var exp = expression
    var preparedExpression = String()
    var i = 0
    while (i < expression.length) {
        if (expression[i].code in 65..127) {
            var operand = String()
            while (expression[i] != ' ' && (expression[i].code in 65..127)) {
                operand += expression[i++]
                if (i == expression.length) break
            }
            if (!heap.isVariableExist(operand)) {
                return Pair("Undefined variable $operand", 0)
            }
            val fromVarToNum = heap.getVariableValue(operand)

            exp = expression.replace(operand, fromVarToNum.toString())
        }
        i++
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