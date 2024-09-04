package com.treevalue


import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserManager(private val userRepository: UserRepository) {
    fun getUserById(id: String): User? {
        return userRepository.findById(id)
    }

    fun createUser(user: User): Boolean {
        if (userRepository.exists(user.id)) {
            return false
        }
        userRepository.save(user)
        return true
    }
}

interface UserRepository {
    fun findById(id: String): User?
    fun save(user: User)
    fun exists(id: String): Boolean
}

data class User(val id: String, val name: String)


class UserManagerTest {
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val userManager = UserManager(userRepository)

    @Test
    fun `getUserById returns user when exists`() {
        val user = User("1", "Alice")
        every { userRepository.findById("1") } returns user

        val result = userManager.getUserById("1")

        assertEquals(user, result)
    }

    @Test
    fun `createUser returns false when user already exists`() {
        val user = User("1", "Alice")
        every { userRepository.exists("1") } returns true

        val result = userManager.createUser(user)

        assertFalse(result)
    }

    @Test
    fun `createUser saves user and returns true when user does not exist`() {
        val user = User("2", "Bob")
        every { userRepository.exists("2") } returns false

        val result = userManager.createUser(user)

        verify { userRepository.save(user) }
        assertTrue(result)
    }
}
