package com.example.maintenance.model;


import jakarta.persistence.*;

@Entity
@Table(name="Issue")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String location;
    private String priority;


    public Long getId()
    {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public void setLocation(String location) {
        this.location = location;
    }
    public Issue() {
    }
}
