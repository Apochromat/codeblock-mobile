package com.apochromat.codeblockmobile.logic

var ok = fun(): String { return "OK" }
var emptyInput = fun(): String { return "Empty Input" }
var inputError = fun(): String { return "InputError" }
var memoryLimit = fun(): String { return "Memory limit" }
var negativeRoot = fun(): String { return "Root of a negative number" }
var zeroDivision = fun(): String { return "Division by zero" }
var indexOutOfRange = fun(): String { return "Index out of range" }
var incorrectExpression = fun(): String { return "Incorrect expression" }
var invalidComparator = fun(): String { return "Invalid comparator" }
var sizesMismatch = fun(): String { return "Sizes mismatch" }
var variableAssignSequence = fun(): String { return "Impossible to assign sequence to variable" }


var unexpectedSymbol = fun(operand: String): String { return "Unexpected symbol '$operand'" }
var undefinedVariable = fun(variableValue: String): String { return "Undefined variable '$variableValue'" }
var undefinedArray = fun(arrayName: String): String { return "Undefined array '$arrayName'" }
var incorrectNaming = fun(name: String): String { return "Incorrect naming '$name'" }
var incorrectSize = fun(size: String): String { return "Incorrect size '$size'" }
var incorrectValue = fun(value: String): String { return "Incorrect value '$value'" }


var typeMismatchVariable = fun(name: String): String { return "Type mismatch, $name is an existing variable" }
var typeMismatchArray = fun(name: String): String { return "Type mismatch, $name is an existing array" }

var tagNaN = fun(): String { return "NaN" }
var tagVariable = fun(): String { return "Variable" }
var tagArray = fun(): String { return "Array" }

var programFinish = fun(status: String): String { return "Program finished with status: $status" }
var programStart = fun(): String { return "Program started" }

var allComparators = listOf(">", ">=", "<", "<=", "==", "!=")

var titleInput = "Введите значение"
