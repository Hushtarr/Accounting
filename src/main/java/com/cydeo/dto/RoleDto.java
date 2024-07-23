package com.cydeo.dto;

import com.cydeo.entity.common.BaseEntity;

import javax.persistence.Entity;

import lombok.*;

import javax.persistence.Table;

public class RoleDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "roles")
    public class Role extends BaseEntity {

        private long id;
        private String description;
    }
}
