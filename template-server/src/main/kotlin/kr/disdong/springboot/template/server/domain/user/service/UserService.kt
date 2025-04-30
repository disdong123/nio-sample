package kr.disdong.springboot.template.server.domain.user.service

import kr.disdong.springboot.template.infrastructure.jpa.domain.user.repository.UserRepository
import kr.disdong.springboot.template.server.domain.user.dto.CreateUserBody
import kr.disdong.springboot.template.server.domain.user.dto.CreateUserResponse
import kr.disdong.springboot.template.server.domain.user.exception.UserNotFound
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun getByUserId(userId: Long) =
        CreateUserResponse.of(
            userRepository.findByUserId(userId)
                ?: throw UserNotFound(userId)
        )

    @Transactional
    fun create(request: CreateUserBody): CreateUserResponse {
        return CreateUserResponse.of(userRepository.save(request.toUserEntity()))
    }
}
