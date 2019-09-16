package com.example.hellosustechbackend.status;

/**
 * The result of the post or get operation from
 * the client.
 */
public class Status {
    private boolean result;
    private String description;

    /**
     * Default constructor
     */
    public Status(){
        this.result = false;
        this.description = "";
    }

    /**
     * Constructor with initial values
     *
     * @param result the result
     * @param description the description of result
     */
    public Status(boolean result,String description){
        this.result = result;
        this.description = description;
    }

    /**
     * Get the result
     *
     * @return  {@code true} if the operation is successful,
     *          {@code false} otherwise.
     */
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
