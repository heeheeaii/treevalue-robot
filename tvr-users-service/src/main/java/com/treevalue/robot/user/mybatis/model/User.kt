package com.treevalue.robot.user.mybatis.model


import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.io.Serializable

@TableName("tvr_users")
data class User(


    @TableField(value = "name")
    @NotBlank(message = "user name can't is null")
    @Size(max = 100, message = "use name length can't longer than 100 characters")
    var name: String,

    @TableField(value = "pwd")
    @NotBlank(message = "password can't is null")
    @Size(max = 50, message = "password length can't longer than 50 characters")
    var pwd: String,

    @TableField(value = "email")
    @NotBlank(message = "email can't is null")
    @Email(message = "email format error")
    var email: String,
) : Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    var id: String? = null
}

