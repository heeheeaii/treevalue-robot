package com.treevalue.robot.exception

class FileSizeOutBound(message: String) : Exception(message) {
    constructor() : this("File size exceeds the maximum allowed limit.")
}
