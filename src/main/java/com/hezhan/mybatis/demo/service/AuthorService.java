package com.hezhan.mybatis.demo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hezhan.mybatis.demo.entity.po.Author;
import com.hezhan.mybatis.demo.entity.to.AuthorTO;
import com.hezhan.mybatis.demo.entity.vo.NovelAuthorVO;

import java.util.List;

public interface AuthorService extends IService<Author> {
    /**
     * 从缓存中获取作者数据，
     * 若缓存中没有则调用数据库获取所有作者数据
     *
     * @return
     */
    List<AuthorTO> getAuthorList();

    /**
     * 获取作者名称和小说名称关联数据
     *
     * @return
     */
    List<NovelAuthorVO> getNovelAuthorTable();

    /**
     * 新增作者信息数据
     *
     * @param author
     * @return
     */
    boolean insertAuthor(Author author);

    /**
     * 更新数据（全字段更新）
     *
     * @param author
     * @return
     */
    boolean updateAuthor(Author author);

    /**
     * 测试mybatis plus的批量导入功能
     */
    void insertBatchAuthor();

    /**
     * 测试springboot事务注解的作用范围，如果此方法调用了另一个没加注解的方法，
     * 而被调用的方法中也有sql语句，这两个方法里的sql语句会不会成为一个事务
     * @param flag 当前方法异常启动标识，如果传1，则模拟异常
     * @param status 方法里另外调用的另一个方法的方法异常启动标识
     */
    void testAffairs(int flag, int status);
}
