package com.example.blockchainclient1.mapper;

import com.example.blockchainclient1.bean.Data;
import com.example.blockchaincore.rdb.mapper.BCMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Mapper
public interface DBMapper extends BCMapper {

    @Select("SELECT * FROM db_data.t_data WHERE c_bh=#{id}")
    @Results({
            @Result(property = "bh", column = "c_bh"),
            @Result(property = "ah", column = "c_ah"),
            @Result(property = "jarq",column = "dt_jarq"),
            @Result(property = "update",column = "dt_update")
    })
    Data getDataById(@Param("id") String id);

    @Insert("INSERT INTO db_data.t_data VALUES(#{bh},#{ah},#{jarq},#{update}) ")
    void addData(Data data);

    @Update("UPDATE db_data.t_data SET dt_jarq=#{jarq} WHERE c_bh=#{id}")
    void updateJarqById(@Param("jarq") Date jarq, @Param("id") String id);

    @Delete("DELETE FROM db_data.t_data WHERE c_bh=#{id}")
    void deleteDataById(@Param("id") String id);
}
