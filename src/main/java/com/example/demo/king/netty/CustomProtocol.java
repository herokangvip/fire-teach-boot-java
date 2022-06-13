package com.example.demo.king.netty;

import java.io.Serializable;

/**
 * server
 *
 * @author sandykang
 */

public class CustomProtocol implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String content;

    public CustomProtocol() {
    }

    public CustomProtocol(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
