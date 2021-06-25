package kr.makeajourney.authentication.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Profile("!prod")
@Configuration
@EnableSwagger2
class SwaggerConfig {

    private fun apiKey(): ApiKey =
        ApiKey("JWT", "Authorization", "header")

    private fun securityContext(): SecurityContext =
        SecurityContext.builder().securityReferences(defaultAuth()).build()

    private fun defaultAuth(): List<SecurityReference> =
        listOf(SecurityReference("JWT", arrayOf(AuthorizationScope("global", "accessEverything"))))

    @Bean
    fun api(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage("kr.makeajourney.authentication"))
            .paths(PathSelectors.any())
            .build()
}
