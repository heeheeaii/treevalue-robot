package com.treevalue.robot.user.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.nio.charset.StandardCharsets
import java.util.*


object JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定JWT_SEC秘钥
     *
     * @param jwtSec    jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param ttlMillis jwt过期时间(毫秒)
     * @param username  用户名 可根据需要传递的信息添加更多, 因为浏览器get传参url限制，不建议放置过多的参数
     * @return
     */
    val jwtSec: String =
        "treevalueksdjhkj348ertgjhdkkj03w94idkc49e856t09w45t88uthgclbodiu940e4sdjksdt43958904ugdu2988234r34Asjkjd"
    val ttlMillis: Long = 1800000L

    fun generateJWT(username: String?):String{
        return createJWT(jwtSec, ttlMillis,username)
    }
    fun parseJWT(jwt:String):String{
        return parseJWT(jwtSec,jwt).subject
    }

    fun createJWT(jwtSec: String, ttlMillis: Long, username: String?): String {
        // 指定签名的时候使用的签名算法，也就是header那部分
        val signatureAlgorithm = SignatureAlgorithm.HS256

        // 生成JWT的时间
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)

        // 创建payload的私有声明（根据特定的业务需要添加）
        val claims: MutableMap<String, Any?> = HashMap()
        claims["username"] = username

        // 添加payload声明
        // 设置jwt的body
        val builder: JwtBuilder =
            Jwts.builder() // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims) // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString()) // iat: jwt的签发时间
                .setIssuedAt(now) // 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串
                .setSubject(username) // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, jwtSec.toByteArray(StandardCharsets.UTF_8))
        if (ttlMillis >= 0) {
            val expMillis = nowMillis + ttlMillis
            val exp = Date(expMillis)
            // 设置过期时间
            builder.setExpiration(exp)
        }
        return builder.compact()
    }

    /**
     * Token的解密
     *
     * @param jwtSec jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token  加密后的token
     * @return
     */
    fun parseJWT(jwtSec: String, token: String?): Claims {
        // 得到DefaultJwtParser
        return Jwts.parser() // 设置签名的秘钥
            .setSigningKey(jwtSec.toByteArray(StandardCharsets.UTF_8)) // 设置需要解析的jwt
            .parseClaimsJws(token).getBody()
    }
}

fun main() {
    val jwt = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3R0ZXN0IiwianRpIjoiNTczNjRhMDctMzVlMy00YjQyLWFkN2EtOWEyYjBlZjYxZDhlIiwiaWF0IjoxNzEwODQ0MjgxLCJzdWIiOiJ0ZXN0dGVzdCIsImV4cCI6MTcxMDg0NjA4MX0.Q5WSjtvC14TIr_F_RMEWh5ZFC6wl8vNCcBkL4fPLikU"
//    val abc = JwtUtil.createJWT(JwtUtil.jwtSec, JwtUtil.ttlMillis, "abc")
    val text = JwtUtil.parseJWT(JwtUtil.jwtSec, jwt)
}
