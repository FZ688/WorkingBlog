package com.iblog.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private String status;
    private String msg;
}
