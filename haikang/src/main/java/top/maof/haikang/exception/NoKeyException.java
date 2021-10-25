package top.maof.haikang.exception;

public class NoKeyException extends Exception {
    public NoKeyException(String msg) {
        super(msg);
    }

    public NoKeyException() {
        super("No Primary Key Error 没有设置主键");
    }
}
