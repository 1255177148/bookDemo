package com.hezhan.mybatis.demo.exception;

/**
 * @Author hezhan
 * @Date 2019/10/15 9:56
 */
public class RemoteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RemoteException(){

    }

    public RemoteException(String message){
        super(message);
    }

    public RemoteException(String message, Throwable cause){
        super(message, cause);
    }
}
