package com.example.xuversion.model;

public class Status {
    private boolean result;
    private String description;

    /**
     * Status
     * @param result the result
     * @param description the description
     */
    public Status(boolean result, String description){
        this.result = result;
        this.description = description;
    }

    /**
     * if it is result
     * @return true or false
     */
    public boolean isResult() {
        return result;
    }

    /**
     * set result
     * @param result the result to set
     */
    public void setResult(boolean result) {
        this.result = result;
    }

    /**
     * description getter
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * description setter
     * @param description set description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
