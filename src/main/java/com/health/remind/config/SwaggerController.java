package com.health.remind.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author QQQtx
 * @since 2024/12/12 12:42
 */
@RestController
public class SwaggerController {
    @GetMapping("/")
    public String root() {
        return "Swagger-UI is running. Visit /doc.html for API docs.";
    }
}
