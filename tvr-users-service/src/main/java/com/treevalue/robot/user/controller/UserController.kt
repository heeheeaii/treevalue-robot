package com.treevalue.robot.user.controller

import com.treevalue.robot.user.exception.EmailHadSinUpException
import com.treevalue.robot.user.exception.UnfinishedTimeException
import com.treevalue.robot.user.exception.UserNameHadSinUpException
import com.treevalue.robot.user.model.UserSign
import com.treevalue.robot.user.mybatis.model.User
import com.treevalue.robot.user.mybatis.repository.UserMapper
import com.treevalue.robot.user.redis.RedisPoolUtil
import com.treevalue.robot.user.util.CryptUtils
import com.treevalue.robot.user.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.random.Random


@RestController
@RequestMapping("/users")
class UserController(
    val userMapper: UserMapper,
) {
    val jwtUtil: JwtUtil = JwtUtil
    private val LOGGER = LoggerFactory.getLogger(UserController::class.java)

    private fun redisStoreIpEmailVerificationTiming(ip: String?, emailOrName: String, verification: String) {
        var con = RedisPoolUtil.getConnection()
        try {
            val client = con.sync()
            if (client.get(ip) != null) {
                throw UnfinishedTimeException("Verification has not timed out")
            }
            var email = ""
            var user: User? = null
            if (emailOrName.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
                email = emailOrName
            } else {
                user = userMapper.findByName(emailOrName)
                if (user == null) {
                    throw Exception("User with the given name does not exist")
                }
                email = user.email
            }
            sendTo(verification, email)
            client.setex(ip, 60, "$email:$verification")
        } finally {
            RedisPoolUtil.returnConnection(con)
        }

    }

    @GetMapping("/verification/{emailOrName}")
//    now 1: allow origin:*, header: 1)access-control-origin:verification;2)verification
//    todo fixed cors problem
    fun getVerificationByJsonMessage(
        @PathVariable("emailOrName") emailOrName: String,
        request: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<String> {
        var res: ResponseEntity<String> = ResponseEntity.ok().body("")
        val verification = Random.nextInt(100000, 1000000).toString()
        try {
            redisStoreIpEmailVerificationTiming(request.remoteHost, emailOrName, verification)
            sendTo(verification, emailOrName)
            val headers = HttpHeaders()
            headers.set("Access-Control-Expose-Headers", "verification")
            headers.set("verification", verification)
            res = ResponseEntity.ok().headers(headers).body("$verification")
        } catch (e: UnfinishedTimeException) {
            res = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("request too frequently")
        }
        return res
    }

    @GetMapping("/verification")
    @Deprecated(message = "headers can't work normally always")
//    now 1: allow origin:*, header: 1)access-control-origin:verification;2)verification
//    todo fixed cors problem
    fun getVerification(
        @RequestHeader("emailOrName") emailOrName: String,
        request: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<String> {
        var res: ResponseEntity<String> = ResponseEntity.ok().body("")
        val verification = Random.nextInt(100000, 1000000).toString()
        try {
            redisStoreIpEmailVerificationTiming(request.remoteHost, emailOrName, verification)
            sendTo(verification, emailOrName)
            val headers = HttpHeaders()
            headers.set("Access-Control-Expose-Headers", "verification")
            headers.set("verification", verification)
            res = ResponseEntity.ok().headers(headers).body("$verification")
        } catch (e: UnfinishedTimeException) {
            res = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("request too frequently")
        }
        return res
    }

    private fun sendTo(verification: String, emailOrName: String) {
    }

    @PostMapping("/signIn")
    fun signIn(
        @RequestBody userSign: UserSign,
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<String> {
        val client = RedisPoolUtil.getConnection().sync()
        val emailVerification = client.get(httpServletRequest.remoteHost)
        return if (emailVerification == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification does not exist")
        } else {
            client.del(httpServletRequest.remoteHost)
            val parts = emailVerification.split(":")
            val storedVerification = parts.last()
            if (storedVerification != userSign.verification) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification error")
            } else {
                val findUser = userSign.name?.let { userMapper.findByName(it) }
                    ?: userSign.email?.let { userMapper.findByEmail(it) }

                if (findUser == null || !CryptUtils.checkPassword(userSign.pwd, findUser.pwd)) {
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username or password error")
                } else {
                    val token = jwtUtil.generateJWT(findUser.name)
                    client.setex(token, 1800, httpServletRequest.remoteHost)
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", "token")
                    httpServletResponse.setHeader("token", token)
                    ResponseEntity.ok().body("")
                }
            }
        }
    }

    @PostMapping("/signUp")
    fun signUp(
        @RequestBody userSign: UserSign,
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): ResponseEntity<String> {
        var res: ResponseEntity<String>? = null
        checkVerification(httpServletRequest.remoteHost, userSign.verification)
        try {
            signUpUser(userSign)
        } catch (e: EmailHadSinUpException) {
            res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user email has exist")
        } catch (e: UserNameHadSinUpException) {
            res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user name has exist")

        } catch (e: Exception) {
            LOGGER.error(e.message)
            res = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("")
        }
        if (res != null) {
            return res
        }

        val token = generateToken(userSign.name!!)
        storeTokenToRedisKeyValue(token, 18000, httpServletRequest.remoteHost)
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "token")
        httpServletResponse.setHeader("token", token)
        return ResponseEntity.ok().body("")
    }

    private fun storeTokenToRedisKeyValue(token: String, expiration: Long, ip: String) {
        val con = RedisPoolUtil.getConnection()
        try {
            val client = con.sync()
            client.setex(token, expiration, ip)
        } finally {
            RedisPoolUtil.returnConnection(con)
        }
    }

    private fun signUpUser(userSign: UserSign) {
        userSign.name?.let { name ->
            if (userMapper.findByName(name) != null) {
                throw UserNameHadSinUpException("User with the given name already signed up")
            }
        }
        userSign.email?.let { email ->
            if (userMapper.findByEmail(email) != null) {
                throw EmailHadSinUpException("User with the given email already signed up")
            }
        }
        userSign.pwd = CryptUtils.encryptPassword(userSign.pwd)
        userMapper.insert(User(userSign.name!!, userSign.pwd, userSign.email!!))
    }

    private fun checkVerification(ip: String, verification: String): Boolean {
        RedisPoolUtil.getConnection().use { con ->
            val client = con.sync()
            val emailVerification = client.get(ip)

            return if (emailVerification == null) {
                false
            } else {
                client.del(ip)
                val parts = emailVerification.split(":")
                val storedVerification = parts.last()
                verification == storedVerification
            }
        }
    }

    private fun generateToken(name: String): String {
        return jwtUtil.generateJWT(name)
    }

    @GetMapping("/tokenToId/{token}")
    fun tokenToId(@PathVariable("token") token: String): ResponseEntity<String> {
        val username = jwtUtil.parseJWT(token)
        if (username == null) return return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val user = userMapper.findByName(username)
        return ResponseEntity.ok(user.id)
    }
}
