package com.example.demo.king.netty.nettys;

import org.msgpack.annotation.Ignore;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author k
 * @version 1.0
 * @date 2019/11/24 21:13
 */
@Message
public class NettyMessage implements Serializable {
    @Ignore
    private static final long serialVersionUID = -1L;

    private Integer length;


    private Byte type;

    private Map<String, String> body = new HashMap<>();

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }
}
