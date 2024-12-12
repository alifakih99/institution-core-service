package com.itg.institution.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response
{
    private String message;
    private boolean status;
    private int code;
    private Object data;
}
