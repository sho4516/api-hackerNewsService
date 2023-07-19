package com.questionpro.hackerNewsService.Payload.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ItemDto {
    private String by;

    @JsonIgnore
    private int id;
    private int score;
    private int time;
    private String title;
    private String url;
    private String type;
    private int descendants;
    private List<Integer> kids;
    private String text;
    private boolean deleted;

    public String getType() {
        return type;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setId(int id) {
        this.id = id;
    }

    public int getDescendants() {
        return descendants;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }

    public List<Integer> getKids() {
        return kids;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setKids(List<Integer> kids) {
        this.kids = kids;
    }

    public String getText() {
        return text;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setText(String text) {
        this.text = text;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
