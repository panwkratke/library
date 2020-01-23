package com.example.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractEntity {

    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
    }
}
