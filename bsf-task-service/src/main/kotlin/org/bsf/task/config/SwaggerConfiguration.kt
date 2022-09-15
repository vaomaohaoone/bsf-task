package org.bsf.task.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfiguration {

    private fun apiInfo() =
        ApiInfo(
            "Accounts REST API",
            "BSF task",
            "v1", "", Contact("", "", ""),
            "", "", listOf()
        )


    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
            .select()
            .paths(regex("/api/v1/.*"))
            .build()
    }

}