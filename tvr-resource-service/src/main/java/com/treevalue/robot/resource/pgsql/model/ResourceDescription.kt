package com.treevalue.robot.resource.pgsql.model

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.treevalue.robot.resource.pgsql.adaptor.TextArrayTypeHandler
import com.treevalue.robot.resource.validation.ListSizeGreaterThanZero
import jakarta.validation.constraints.NotBlank
import org.apache.ibatis.type.JdbcType
import org.springframework.data.annotation.Id
import java.io.Serializable

@TableName("tvr_resource_description")
data class ResourceDescription(
    @NotBlank
    @TableField("resourceid")
    val resourceId: String,
    @ListSizeGreaterThanZero
    @TableField(typeHandler = TextArrayTypeHandler::class, jdbcType = JdbcType.ARRAY)
    val condition: ArrayList<String>,
    @NotBlank
    val usefulness: String,

    @NotBlank
    val price: Double,

    @NotBlank
    val valuation: Double
) : Serializable {
    @Id
    var id: String? = null
}

