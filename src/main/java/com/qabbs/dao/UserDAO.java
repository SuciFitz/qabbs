package com.qabbs.dao;

import com.qabbs.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, head_url, auth ";
    String SELECT_FIELDS = " id, name, password, head_url, auth";

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{name},#{password},#{headUrl},#{auth})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name like '%${name}%'"})
    List<User> getByName(@Param("name") String name);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME})
    List<User> selectAll();

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Update({"update ", TABLE_NAME, " set auth=#{auth} where id=#{id}"})
    void updateauth(User user);

    @Delete({"delete from ", TABLE_NAME, " where name=#{name}"})
    void deleteByName(@Param("name") String name);
}
