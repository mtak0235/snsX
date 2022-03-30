package kr.seoul.snsX.exception;

public class FailImgSaveException extends RuntimeException {
    public FailImgSaveException() {
        super();
    }

    public FailImgSaveException(String message) {
        super(message);
    }
}
