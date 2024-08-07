package br.com.socialties.infra.security;

import br.com.socialties.infra.security.exceptions.AppSecurityException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityExceptionHandler {

    public void handleException(AppSecurityException exception, HttpServletResponse response) throws IOException {
        var message = exception.getMessage()
                .replace("\"", "\\\"");

        response.setContentType("application/json");
        response.setStatus(exception.getHttpStatus().value());
        response.getOutputStream()
                .println("{ \"error\": \"" + message + "\" }");
    }

}
