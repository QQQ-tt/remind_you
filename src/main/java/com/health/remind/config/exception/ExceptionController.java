package com.health.remind.config.exception;

import com.health.remind.config.enums.DataEnums;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qtx
 * @since 2024/3/19
 */
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @RequestMapping("/jwtException")
    public void test(HttpServletRequest request) {
        if (request.getAttribute("jwtException") instanceof AuthException) {
            throw ((AuthException) request.getAttribute("jwtException"));
        } else {
            throw new AuthException(DataEnums.FAILED);
        }
    }
}
