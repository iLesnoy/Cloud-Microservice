package com.petrovskiy.epm.exception;

public class SystemException extends RuntimeException{

    int errorCode;

    public SystemException(int errorCode){
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}