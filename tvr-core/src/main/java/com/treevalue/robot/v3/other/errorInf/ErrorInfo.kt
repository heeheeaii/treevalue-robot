package com.treevalue.robot.v3.other.errorInf

enum class ErrorInfo {
    OK,
    InputTypeError,
    InputRangeError
}

val str: HashMap<ErrorInfo, String> = hashMapOf(
    ErrorInfo.OK to "",
    ErrorInfo.InputTypeError to "input param type don't right"
)
