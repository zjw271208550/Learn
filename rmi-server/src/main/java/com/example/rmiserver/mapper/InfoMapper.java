package com.example.rmiserver.mapper;

import com.example.rmiserver.entity.Info;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InfoMapper {

    @Select("SELECT * FROM db_demo.t_test WHERE c_id={#id}")
    @Results({
            @Result(property = "id", column = "c_id"),
            @Result(property = "name", column = "c_name"),
            @Result(property = "value",column = "n_value"),
            @Result(property = "create",column = "dt_create"),
            @Result(property = "update",column = "dt_update")
    })
    Info getInfoById(@Param("id") String id);

    @Select("SELECT * FROM db_demo.t_test")
    @Results({
            @Result(property = "id", column = "c_id"),
            @Result(property = "name", column = "c_name"),
            @Result(property = "value",column = "n_value"),
            @Result(property = "create",column = "dt_create"),
            @Result(property = "update",column = "dt_update")
    })
    List<Info> getInfos();

    @Update("UPDATE db_demo.t_test SET n_value=#{value} WHERE c_id=#{id}")
    void setInfoValueById(@Param("value") Integer value, @Param("id") String id);

}
