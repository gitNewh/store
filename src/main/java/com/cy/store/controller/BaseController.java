package com.cy.store.controller;

import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

public class BaseController {

    public static final int OK = 200;

    @ExceptionHandler(ServiceException.class)
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof UsernameDuplicatedException) {
            result.setState(4000);

        } else if (e instanceof InsertException) {
            result.setState(5000);
        } else if (e instanceof UserNotFoundException) {
            result.setState(5001);
        } else if (e instanceof PasswordNotMatchException) {
            result.setState(5002);
        } else if (e instanceof UpdateException) {
            result.setState(5003);
        } else if (e instanceof AddressCountLimitException) {
            result.setState(5004);
        }

        return result;
    }

    protected final Integer getuidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    protected final String getUsernameFromSession(HttpSession session) {
        return session.getAttribute("username").toString();
    }


}
