package com.es.exception;import org.springframework.http.HttpStatus;/** * @author <a href="mailto:xinput.xx@gmail.com">xinput</a> * @date 2020-11-13 11:09 */public class ServiceException extends BaseException {    public ServiceException(String message) {        super(message);    }    public ServiceException(String message, Throwable cause) {        super(message, cause);    }    @Override    protected HttpStatus getStatus() {        return HttpStatus.INTERNAL_SERVER_ERROR;    }}