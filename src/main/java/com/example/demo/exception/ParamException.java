package com.example.demo.exception;

/**
 * ParamException
 *
 * @author sandykang
 **/
public class ParamException extends BaseBusinessException {


    private static final long serialVersionUID = -1L;

    public ParamException(int code, String desc) {
        super(code, desc);
    }

    public ParamException(Throwable cause, int code, String desc) {
        super(cause, code, desc);
    }
}
