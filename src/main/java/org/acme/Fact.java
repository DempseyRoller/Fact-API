package org.acme;

import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(database = "main", collection = "facts")
public class Fact {

    enum Creator {
        USER, API;
    }

    public Fact() {
    }

    @JsonProperty("_id")
    private ObjectId id;
    private String text;
    private String type;
    @JsonInclude(Include.NON_DEFAULT)
    private int additions;
    @JsonInclude(Include.NON_NULL)
    private Date firstSavedOn;
    @JsonInclude(Include.NON_NULL)
    private Date lastSavedOn;
    private Creator createdBy = Creator.API;

    public void dbInit() {
        firstSavedOn = new Date();
        lastSavedOn = firstSavedOn;
        additions = 1;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public Date getFirstSavedOn() {
        return firstSavedOn;
    }

    public void setFirstSavedOn(Date firstSavedOn) {
        this.firstSavedOn = firstSavedOn;
    }

    public Date getLastSavedOn() {
        return lastSavedOn;
    }

    public void setLastSavedOn(Date lastSavedOn) {
        this.lastSavedOn = lastSavedOn;
    }

    public Creator getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Creator createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "FactPojo [id=" + id + ", text=" + text + ", type=" + type + ", additions=" + additions
                + ", firstSavedOn=" + firstSavedOn + ", lastSavedOn=" + lastSavedOn + "]";
    }

}
