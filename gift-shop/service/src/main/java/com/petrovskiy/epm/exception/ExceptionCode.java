package com.petrovskiy.epm.exception;

public class ExceptionCode {

    public static final int BAD_REQUEST = 40000;
    public static final int UNREADABLE_MESSAGE = 40001;
    public static final int EMPTY_OBJECT = 40010;
    public static final int NON_EXISTENT_ENTITY = 40410;
    public static final int NON_EXISTENT_PAGE = 40051;
    public static final int TAG_INVALID_NAME = 40020;
    public static final int CERTIFICATE_INVALID_NAME = 40030;
    public static final int CERTIFICATE_INVALID_DESCRIPTION = 40031;
    public static final int CERTIFICATE_INVALID_PRICE = 40032;
    public static final int CERTIFICATE_INVALID_DURATION = 40033;
    public static final int INVALID_ATTRIBUTE_LIST = 40034;
    public static final int FORBIDDEN_ACCESS = 40310;
    public static final int USER_INVALID_NAME = 40320;
    public static final int INVALID_CREDENTIALS = 40040;
    public static final int USED_ENTITY = 40910;
    public static final int DUPLICATE_NAME = 40911;
    public static final int DATA_BASE_ERROR = 50300;
    public static final int USER_INVALID_PASSWORD = 40041;

    private ExceptionCode() {
    }
}
