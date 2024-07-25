package com.hdu.message.manager.handler;

import com.hdu.message.common.base.exception.ManagerMessageException;
import com.hdu.message.common.base.exception.SectionException;
import com.hdu.message.common.base.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义全局异常捕获器
 */
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
        log.warn("request {} throw ManagerSectionException \n", request, sectionException);
        return ResponseUtil.fail(-1, sectionException.getErrorCode(), sectionException.getErrorMessage());
    }

    @ExceptionHandler(Exception.class)
    public Object handlerException(Exception exception, HttpServletRequest request) {
        log.warn("request {} throw Exception \n", request, exception);
        return ResponseUtil.fail(exception.getMessage());
    }
}
