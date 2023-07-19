package com.questionpro.hackerNewsService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Story")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pkItem;

    @Column(name = "CreatedBy")
    private String by;

    @Column(name = "storyId")
    private int storyId;

    @Column
    private int score;

    @Column
    private int time;

    @Column
    private String title;

    @Column
    private String type;

    @Column
    private String url;
}
