package kr.disdong.springboot.template.server.domain.user.controller.spec

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.disdong.springboot.template.common.dto.TemplateResponse
import kr.disdong.springboot.template.server.domain.user.dto.CreateUserBody
import kr.disdong.springboot.template.server.domain.user.dto.CreateUserResponse

@Tag(name = "유저")
interface UserSpec {

    @Operation
    fun getByUserId(userId: Long): TemplateResponse<CreateUserResponse>

    @Operation
    fun create(body: CreateUserBody): TemplateResponse<CreateUserResponse>
}
