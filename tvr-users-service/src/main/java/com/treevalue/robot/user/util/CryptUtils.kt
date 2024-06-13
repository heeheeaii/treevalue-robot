package com.treevalue.robot.user.util

import org.mindrot.jbcrypt.BCrypt


object CryptUtils {
    private val WORK_FACTOR = 12
    fun encryptPassword(plainTextPassword: String?): String {
        val salt: String = BCrypt.gensalt(WORK_FACTOR)
        return BCrypt.hashpw(plainTextPassword, salt)
    }

    fun checkPassword(plainTextPassword: String?, hashedPasswordFromDatabase: String?): Boolean {
        return BCrypt.checkpw(plainTextPassword, hashedPasswordFromDatabase)
    }
}
