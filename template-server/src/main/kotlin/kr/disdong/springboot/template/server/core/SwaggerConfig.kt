package kr.disdong.springboot.template.server.core

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
@OpenAPIDefinition(servers = [Server(url = "/api", description = "Default Server URL")])
class SwaggerConfig {
    @Bean
    fun openAPI(@Value("v1.0") appVersion: String?): OpenAPI {
        val info = Info()
            .title("Template!")
            .version(appVersion)
            .description("Template!")

        // 인증
        val security = SecurityRequirement().addList("AccessToken")

        val components = Components()
            .addSecuritySchemes(
                "AccessToken",
                SecurityScheme()
                    .name("AccessToken")
                    .description("Access Token 혹은 휴대폰번호를 입력하면 됩니다.")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )

        return OpenAPI()
            .info(info)
            .addSecurityItem(security)
            .components(components)
    }
}
