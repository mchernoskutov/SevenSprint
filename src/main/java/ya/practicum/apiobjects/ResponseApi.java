package ya.practicum.apiobjects;

public class ResponseApi {

    private int code;
    private String message;
    private boolean ok;

    public ResponseApi() {

    }

    public ResponseApi(int code, String message, boolean ok) {
        this.code = code;
        this.message = message;
        this.ok = ok;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
