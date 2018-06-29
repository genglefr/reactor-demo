package com.genglefr.webflux.demo.model;

public class EmptyEntity implements Entity {

    private String id;
    private String resourceType;

    public EmptyEntity(String id, String resourceType) {
        this.id = id;
        this.resourceType = resourceType;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getResourceType() {
        return this.resourceType;
    }
}
