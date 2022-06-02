package com.hezhan.mybatis.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hezhan.mybatis.demo.entity.po.Author;
import com.hezhan.mybatis.demo.entity.to.AuthorTO;
import com.hezhan.mybatis.demo.entity.to.NovelAuthorTO;
import com.hezhan.mybatis.demo.entity.vo.NovelAuthorVO;
import com.hezhan.mybatis.demo.mapper.AuthorMapper;
import com.hezhan.mybatis.demo.service.AuthorService;
import com.hezhan.mybatis.demo.util.RedisUtil;
import com.hezhan.mybatis.demo.util.RestTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @Author hezhan
 * @Date 2020/3/6 16:47
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements AuthorService {

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthorMapper authorMapper;

    @Value("${redis.key.prefix}")
    private String keyPrefix;

    @Override
    public List<AuthorTO> getAuthorList() {
        // 先从缓存中获取数据，若缓存中无数据，则再查数据库
        String key = getAuthorListKey();
        String value = redisUtil.get(key);
        if (StringUtils.isNotBlank(value)) {
            return JSONArray.parseArray(value, AuthorTO.class);
        }
        /*
          从这里开始，就是如果从上面的代码中拿不到缓存，
          则从数据库中查询，然后封装完数据后写入到缓存中，并返回
         */
        List<Author> authors = authorMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(authors)) {
            return new ArrayList<>();
        }
        List<AuthorTO> authorTOS = new ArrayList<>();
        for (Author author : authors) {
            AuthorTO authorTO = new AuthorTO();
            authorTO.setCode(author.getCode());
            authorTO.setName(author.getName());
            authorTOS.add(authorTO);
        }
        // 写入到缓存中
        redisUtil.set(key, JSON.toJSONString(authorTOS));
        // 返回结果
        return authorTOS;
    }

    @Override
    public List<NovelAuthorVO> getNovelAuthorTable() {
        String url = "http://127.0.0.1:8080/api/getNovelAuthor";
        String response = restTemplateUtil.get(url, String.class, null);
        List<NovelAuthorTO> novelAuthorTOS = JSONArray.parseArray(response, NovelAuthorTO.class);
        if (CollectionUtils.isEmpty(novelAuthorTOS)) {
            return new ArrayList<>();
        }
        List<NovelAuthorVO> authorVOS = new ArrayList<>();
        List<Author> authors = authorMapper.selectList(new QueryWrapper<>());
        for (NovelAuthorTO novelAuthorTO : novelAuthorTOS) {
            NovelAuthorVO novelAuthorVO = new NovelAuthorVO();
            for (Author author : authors) {
                if (novelAuthorTO.getAuthorCode().equalsIgnoreCase(author.getCode())) {
                    novelAuthorVO.setAuthorName(author.getName());
                    break;
                }
            }
            novelAuthorVO.setNovelName(novelAuthorTO.getNovelName());
            authorVOS.add(novelAuthorVO);
        }
        return authorVOS;
    }

    @Override
    public boolean insertAuthor(Author author) {
        Author author1 = authorMapper.selectById(1);
        int id = author1.getId();
        System.out.println("id=" + id);
        author.setCode(author.getCode());
        author.setName(author.getName());
        int result = authorMapper.insert(author1);
        if (result < 0) {
            return false;
        }
        System.out.println("再次的id=" + author1.getId());
        return true;
    }

    @Override
    public boolean updateAuthor(Author author) {
        // 第一种全字段更新方法：在代码中将为null的字段附上空字符串的值，注意，null和""不是一个概念
        if (StringUtils.isBlank(author.getName())) {
            author.setName("");
        }
        // 第一种方法 end
        return updateById(author);
    }

    @Override
    public void insertBatchAuthor() {
        Author author = null;
        List<Author> authorList = new ArrayList<>();
        for (int i = 0; i < 20000; i++) {
            author = new Author();
            author.setCode(UUID.randomUUID().toString());
            author.setName(String.valueOf(i));
            authorList.add(author);
        }
        long start = System.currentTimeMillis();
        saveBatch(authorList, 5000);
//        authorMapper.insertBatchAuthor(authorList);
        long end = System.currentTimeMillis();
        System.out.println("批量导入一共花费了" + (end - start) + "毫秒");
    }

    @Transactional
    @Override
    public void testAffairs(int flag, int status) {
//        Author author = new Author();
//        author.setName("测试1");
//        author.setCode("测试1");
//        BigDecimal bigDecimal = new BigDecimal("1.1");
//        author.setValue(bigDecimal);
//        authorMapper.insert(author);
//        insertValue(status);
        if (flag == 1){
            throw new RuntimeException("模拟异常");
        }
        insertValue(status);
    }

    /**
     * 获取作者列表数据的缓存key
     *
     * @return
     */
    private String getAuthorListKey() {
        StringBuffer key = new StringBuffer(keyPrefix);
        key.append("author.list");
        return key.toString();
    }

    /**
     * 测试springboot事务注解的作用范围，作为另一个被调用的方法
     */
    public void insertValue(int status){
        Author author = new Author();
        author.setName("测试2");
        author.setCode("测试2");
        BigDecimal bigDecimal = new BigDecimal("2.2");
        author.setValue(bigDecimal);
        authorMapper.insert(author);
        if (status == 1){
            throw new RuntimeException("模拟异常2");
        }
    }
}
