package kr.disdong.springboot.template.server.domain.user.controller

import kr.disdong.springboot.template.common.dto.TemplateResponse
import kr.disdong.springboot.template.server.domain.user.controller.spec.UserSpec
import kr.disdong.springboot.template.server.domain.user.dto.CreateUserBody
import kr.disdong.springboot.template.server.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) : UserSpec {

    @GetMapping("/users/{userId}")
    override fun getByUserId(
        @PathVariable userId: Long,
    ) = TemplateResponse.of(userService.getByUserId(userId))

    @PostMapping("/users")
    override fun create(
        body: CreateUserBody,
    ) = TemplateResponse.of(HttpStatus.CREATED, userService.create(body))
}
