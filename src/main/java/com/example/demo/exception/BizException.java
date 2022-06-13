package com.example.demo.exception;

/**
 * ParamException
 *
 * @author sandykang
 **/
public class BizException extends BaseBusinessException {

    private static final long serialVersionUID = -1L;

    public BizException(int code, String desc) {
        super(code, desc);
    }

    public BizException(Throwable cause, int code, String desc) {
        super(cause, code, desc);
    }
}

