package com.king.train.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.king.train.entities.TbUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<TbUser> {
}
