package com.lym.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lym.entity.User;

@Mapper
public interface UserDao {
    
    @Insert("insert into user(username,nickname,password,email,idcard,headpic,enabled,createat,updateat,activestate,token,tokenenabletime) values (#{username},#{nickname},#{password},#{email},#{idcard},#{headpic},#{enabled},#{createat},#{updateat},#{activeState},#{token},#{tokenEnableTime})")
    int add(User user);

    @Delete("delete from user where id=#{id}")
    int delete(int id);
    
    @Update("update user set nickname=#{nickname},password=#{password},email=#{email},idcard=#{idcard},headpic=#{headpic},enabled=#{enabled},updateat=#{updateat} WHERE id=#{id}")
    int update(User user);
    
    @Select("select * from user where id=#{id}")
    @Results({
        @Result(property="username", column="USERNAME", id=true),
        @Result(property="nickname", column="NICKNAME"),
        @Result(property="email", column="EMAIL"),
        @Result(property="idcard", column="IDCARD"),
        @Result(property="headpic", column="HEADPIC"),
        @Result(property="enabled", column="ENABLED"),
        @Result(property="createat", column="CREATEAT"),
        @Result(property="updateat", column="UPDATEAT"),
    })
    User get(int id);
    
    @Select("select * from user where username=#{username}")
    @Results({
        @Result(property="id", column="id"),
        @Result(property="nickname", column="NICKNAME"),
        @Result(property="email", column="EMAIL"),
        @Result(property="idcard", column="IDCARD"),
        @Result(property="headpic", column="HEADPIC"),
        @Result(property="enabled", column="ENABLED"),
        @Result(property="createat", column="CREATEAT"),
        @Result(property="updateat", column="UPDATEAT"),
        @Result(property="activeState", column="ACTIVESTATE"),
        @Result(property="token", column="TOKEN"),
        @Result(property="tokenEnableTime", column="TOKENENABLETIME"),
    })
    User getByUsername(String username);
}
