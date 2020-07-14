package com.hezhan.mybatis.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hezhan.mybatis.demo.entity.po.Author;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthorMapper extends BaseMapper<Author> {

    void insertBatchAuthor(@Param("authors")List<Author> authors);
}
