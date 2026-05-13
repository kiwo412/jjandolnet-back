package com.kin.jjandolnet.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SWAGGER_DESCRIPTION =
            "<h3>[API 테스트 권장 순서]</h3>" +
                    "1. <b>회원가입</b>: UserController - <code>/api/v1/user/signup</code><br>" +
                    "2. <b>로그인</b>: AuthController - <code>/api/v1/auth/login</code><br>" +
                    "3. 로그인 응답의 'accessToken' 값을 복사합니다.<br>" +
                    "4. 화면 우측의 Authorize 버튼을 클릭 후 텍스트 박스에 붙여 넣은 뒤 팝업 속 <br>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;Authorize 버튼을 클릭합니다.<br>" +
                    "5. api 테스트를 진행하시면 됩니다.<br><br>" +
                    "회원 가입 및 로그인 후 다른 API 테스트를 권장합니다.";

    String jwtSchemeName = "accessToken";
    SecurityScheme securityScheme = new SecurityScheme()
            .name(jwtSchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("짠돌넷 API 명세서")
                        .description(SWAGGER_DESCRIPTION)
                        .version("v1.0.0"))
                //.addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes(jwtSchemeName, securityScheme));
    }

}
