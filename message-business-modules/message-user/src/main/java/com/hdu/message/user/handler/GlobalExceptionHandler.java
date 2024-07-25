package com.hdu.message.user.handler;

import com.hdu.message.common.base.exception.ManagerMessageException;
import com.hdu.message.common.base.exception.SectionException;
import com.hdu.message.common.base.exception.UserMessageException;
import com.hdu.message.common.base.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ManagerMessageException.class)
    public Object handlerManagerMessageException(ManagerMessageException managerMessageException, HttpServletRequest request) {
        log.warn("request {} throw ManagerMessageException \n", request, managerMessageException);
        return ResponseUtil.fail(-1, managerMessageException.getErrorCode(), managerMessageException.getErrorMessage());
    }

    @ExceptionHandler(SectionException.class)
    public Object handlerManagerSectionException(SectionException sectionException, HttpServletRequest request) {
        log.warn("request {} throw SectionException \n", request, sectionException);
        System.out.println("执行了");
        return ResponseUtil.fail(-1, sectionException.getErrorCode(), sectionException.getErrorMessage());
    }

    @ExceptionHandler(UserMessageException.class)
    public Object handlerUserMessageException(UserMessageException userMessageException, HttpServletRequest request) {
        log.warn("request {} throw UserMessageException \n", request, userMessageException);
        return ResponseUtil.fail(-1, userMessageException.getErrorCode(), userMessageException.getErrorMessage());
    }

}
