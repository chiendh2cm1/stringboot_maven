package com.example.demonew.model.dto;

import lombok.Data;

@Data
public class CustomPage {
    Object content;
    Object pageable;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getPageable() {
        return pageable;
    }

    public void setPageable(Object pageable) {
        this.pageable = pageable;
    }
}
