package kr.seoul.snsX.exception;

public class ImageOverUploadedException extends RuntimeException {

    public ImageOverUploadedException(String message) {
        super(message);
    }

    public ImageOverUploadedException() {
        super();
    }

    public ImageOverUploadedException(String message, Throwable cause) {
        super(message, cause);
    }


}
