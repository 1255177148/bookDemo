package com.hezhan.mybatis.demo.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author Zhanzhan
 * @Date 2020/3/14 16:06
 */
@Data
@TableName("author")
public class Author extends Model<Author> {
    @TableId
    private String id;
    private String code;
    private String name;
    private BigDecimal value;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
