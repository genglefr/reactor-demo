package com.genglefr.webflux.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Entity {
    @Id
    private String id;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    private Integer age;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getResourceType() {
        return getClass().getSimpleName();
    }

}
