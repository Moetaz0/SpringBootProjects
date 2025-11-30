package com.cc.project.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String root() {
        return "redirect:/auth/login";
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicy() {
        return "privacy"; // src/main/resources/templates/privacy-policy.html
    }

    @GetMapping("/terms-of-service")
    public String termsOfService() {
        return "terms"; // src/main/resources/templates/terms-of-service.html
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Log error attributes to help diagnose null type/status cases.
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        log.error("/error invoked - status={}, message={}, exception={}, uri={}",
                status, message, exception, uri);

        return "error";
    }

}
