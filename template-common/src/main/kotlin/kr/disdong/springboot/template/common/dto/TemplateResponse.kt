package kr.disdong.springboot.template.common.dto

import kr.disdong.springboot.template.common.exception.TemplateException
import org.springframework.http.HttpStatus

class TemplateResponse<T>(
    val code: HttpStatus,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> of(
            exception: TemplateException
        ): TemplateResponse<T> {
            return TemplateResponse(
                code = exception.getCode(),
                message = exception.message,
            )
        }

        fun <T> of(
            content: T? = null,
        ): TemplateResponse<T> {
            return TemplateResponse(
                code = HttpStatus.OK,
                data = content
            )
        }

        fun <T> of(
            code: HttpStatus,
            content: T? = null,
        ): TemplateResponse<T> {
            return TemplateResponse(
                code = code,
                data = content
            )
        }
    }
}
