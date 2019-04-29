package com.fengs.oath2.jpa;


import com.fengs.oath2.orm.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpa extends JpaRepository<Role,String> {

}
