package com.pan.dao;

import com.pan.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username, String password);//虽然继承了很多，也可以自己定义。

}
