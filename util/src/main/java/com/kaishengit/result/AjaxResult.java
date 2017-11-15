package com.kaishengit.result;

/**
 * @author Administrator.
 */
public class AjaxResult {

    public static final String SUCCESS_STATE = "success";
    public static final String ERROR_STATE="error";

    private String state;
    private String message;
    private Object data;

    public static AjaxResult success() {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setState(SUCCESS_STATE);
        return ajaxResult;
    }

    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setState(SUCCESS_STATE);
        ajaxResult.setData(data);
        return ajaxResult;
    }

    public static AjaxResult error(String message) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setState(ERROR_STATE);
        ajaxResult.setMessage(message);
        return ajaxResult;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
