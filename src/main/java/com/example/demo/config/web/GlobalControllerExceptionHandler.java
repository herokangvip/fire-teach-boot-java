package com.example.demo.config.web;


import com.example.demo.common.CommonResult;
import com.example.demo.common.CommonResultCode;
import com.example.demo.exception.BaseBusinessException;
import com.example.demo.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * web全局异常
 *
 * @author sandykang
 */
@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    /**
     * 异常处理
     *
     * @param ex ex
     * @return CommonResult
     */
    @ResponseBody
    @ExceptionHandler(value = BaseBusinessException.class)
    public CommonResult<Object> baseBusinessExceptionHandler(Exception ex) {
        BaseBusinessException exception = (BaseBusinessException) ex;
        CommonResult<Object> result = new CommonResult<>(exception.getCode(), exception.getDesc(), null);
        log.error("errorResult:{}", JsonUtils.toJsonString(result));
        return result;
    }

    /**
     * 异常处理
     *
     * @param ex ex
     * @return CommonResult
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult<Object> errorHandler(Exception ex) {
        CommonResult<Object> result = new CommonResult<>(CommonResultCode.SERVER_ERROR,
                "server error", null);
        log.error("errorResult:{},error:{}", JsonUtils.toJsonString(result), ex);
        return result;
    }
}
