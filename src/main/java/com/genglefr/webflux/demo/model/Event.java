package com.genglefr.webflux.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "events")
@Data
public class Event {
    @Id
    private String id;
    @NotNull
    private String resourceId;
    @NotNull
    private String resourceType;
    @NotNull
    private OperationType operationType;
    @NotNull
    private Date date;
    private Entity resource;

    public Event(Entity resource, OperationType operationType) {
        this.resource = resource;
        this.resourceId = resource.getId();
        this.resourceType = resource.getResourceType();
        this.operationType = operationType;
        this.date = new Date();
    }

    public String getId() {
        return id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Date getDate() {
        return date;
    }

    public Entity getResource() {
        return resource;
    }
}
