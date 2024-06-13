package com.treevalue.robot.user.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.io.Serializable

data class UserSign(

    @Size(max = 100, message = "use name length can't longer than 100 characters")
    var name: String?=null,

    @NotBlank(message = "password can't is null")
    @Size(max = 50, message = "password length can't longer than 50 characters")
    var pwd: String,

    @Email(message = "email format error")
    var email: String?=null,

    @NotBlank(message = "verification can't is null")
    @Size(max = 6, message = "verification length can't longer than 6 characters")
    val verification: String
) : Serializable
