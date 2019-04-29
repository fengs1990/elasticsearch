package com.fengs.oath2.jpa;

import com.fengs.oath2.orm.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpa extends JpaRepository<UserInfo,String> {

    /**
     * 通过用户名称查询用户信息
     * @param userName
     * @return
     */
    @Query("select u from UserInfo u where u.name like :name")
    UserInfo findByUsernameCaseInsensitive(@Param("name") String userName);
}
