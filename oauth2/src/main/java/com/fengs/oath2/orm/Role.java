package com.fengs.oath2.orm;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Role {

    @Id
    @NotNull
    private Long roleId;
    private String roleName;
    private String status;
}
