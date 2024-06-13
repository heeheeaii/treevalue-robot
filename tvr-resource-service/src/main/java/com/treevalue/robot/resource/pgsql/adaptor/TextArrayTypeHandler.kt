package com.treevalue.robot.resource.pgsql.adaptor
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(ArrayList::class)
class TextArrayTypeHandler : BaseTypeHandler<ArrayList<String>>() {

    override fun setNonNullParameter(
        ps: PreparedStatement,
        i: Int,
        parameter: ArrayList<String>,
        jdbcType: JdbcType
    ) {
        val connection = ps.connection
        val sqlArray = connection.createArrayOf("text", parameter.toArray())
        ps.setArray(i, sqlArray)
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): ArrayList<String> {
        val sqlArray = rs.getArray(columnName)
        return sqlArray.let { array ->
            val arrayObj = array.array
            if (arrayObj is Array<*>) {
                return arrayToArrayList(arrayObj)
            }
            return ArrayList()
        }
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): ArrayList<String> {
        val sqlArray = rs.getArray(columnIndex)
        return sqlArray.let {
            val arrayObj = it.array
            if (arrayObj is Array<*>) {
                return arrayToArrayList(arrayObj)
            }
            return ArrayList()
        }
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): ArrayList<String> {
        val sqlArray = cs.getArray(columnIndex)
        return sqlArray.let {
            val arrayObj =it.array
            if (arrayObj is Array<*>) {
                return arrayToArrayList(arrayObj)
            }
            return ArrayList()
        }
    }

    private fun arrayToArrayList(arrayObj: Array<*>): ArrayList<String> {
        val list = ArrayList<String>()
        for (element in arrayObj) {
            list.add(element as String)
        }
        return list
    }
}
