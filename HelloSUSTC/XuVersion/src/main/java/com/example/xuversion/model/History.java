package com.example.xuversion.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class History extends RealmObject {
    private String history;

    @PrimaryKey
    private long id;

    /**
     * Constructor
     */
    public History(){

    }

    /**
     * Constructor
     * @param id id
     * @param history the history
     */
    public History(long id, String history) {
        this.history = history;
        this.id = id;
    }

    /**
     * History getter
     * @return the history
     */
    public String getHistory() {
        return history;
    }
}
