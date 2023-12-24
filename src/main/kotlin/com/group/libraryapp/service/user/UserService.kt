package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun saveUser(request: UserCreateRequest) {
        val newUser = User(request.name, request.age)
        userRepository.save(newUser)
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        return userRepository.findAll()
            .map { user -> UserResponse(user) }
    }

    @Transactional
    fun updateUserName(request: UserUpdateRequest) {
        // 기존 crudRepository로는 optional 형태를 내가 제어할 수 없지만
        // val user = userRepository.findById(request.id).orElseThrow(::IllegalArgumentException)
        // kotlin과의 사용성을 고려해 만들어둔 findByIdOrNull을 사용하면 똑같이 사용할 수 있다
        // val user = userRepository.findByIdOrNull(request.id) ?: fail()
        // 하지만 여기서 한번더 kotlin의 확장함수를 만들어서 더 간단하게 표현할 수 있다
        val user = userRepository.findByIdOrThrow(request.id)
        user.updateName(request.name)
    }

    @Transactional
    fun deleteUser(name: String) {
        val user = userRepository.findByName(name) ?: fail()
        userRepository.delete(user)
    }

}
