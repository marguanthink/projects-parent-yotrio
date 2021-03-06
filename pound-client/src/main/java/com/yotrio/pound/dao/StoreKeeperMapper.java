package com.yotrio.pound.dao;

import com.yotrio.pound.model.StoreKeeper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StoreKeeperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreKeeper record);

    int insertSelective(StoreKeeper record);

    StoreKeeper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StoreKeeper record);

    int updateByPrimaryKey(StoreKeeper record);
}