package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;

import javax.persistence.Entity;

import lombok.*;
import javax.persistence.Table;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private String description;
}

