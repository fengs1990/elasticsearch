package com.fengs.oath2.orm;

import javax.persistence.*;
import java.util.Set;

/**
 * 用户基本信息表
 */
@Entity
public class UserInfo {

    @Id
    @Column(updatable = false,nullable = false)
    private Long userId;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "real_name")
    private String realName;
    @Column(name = "mobile")
    private String mobile;
    private String status;
    private String loginTime;

    /**
     * 用户拥有角色
     */
    @ManyToMany
    @JoinTable(
            name = "sb_user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
