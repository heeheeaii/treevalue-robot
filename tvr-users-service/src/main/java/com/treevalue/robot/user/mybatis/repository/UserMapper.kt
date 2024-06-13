package com.treevalue.robot.user.mybatis.repository

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.treevalue.robot.user.mybatis.model.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface UserMapper : BaseMapper<User> {
    fun findByName(@Param("name") name: String): User
    fun findByEmail(@Param("email") email: String): User
}
