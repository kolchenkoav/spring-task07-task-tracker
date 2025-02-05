package com.example.webfluxexample.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * Конфигурация для обработки ошибок в маршрутизаторе.
 */
@Configuration
@Slf4j
public class RouterErrorConfiguration {

    /**
     * Bean для настройки атрибутов ошибок.
     *
     * @return экземпляр DefaultErrorAttributes с настроенными атрибутами ошибок.
     */
    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
                errorAttributes.put("error-attribute", "Error in request!");
                errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
                return errorAttributes;
            }
        };
    }
}
