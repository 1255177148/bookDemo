package com.hezhan.mybatis.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hezhan.mybatis.demo.entity.po.Author;
import com.hezhan.mybatis.demo.entity.to.AuthorTO;
import com.hezhan.mybatis.demo.entity.to.NovelAuthorTO;
import com.hezhan.mybatis.demo.entity.vo.NovelAuthorVO;
import com.hezhan.mybatis.demo.mapper.AuthorMapper;
import com.hezhan.mybatis.demo.service.AuthorService;
import com.hezhan.mybatis.demo.util.RestTemplateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    private AuthorMapper authorMapper;

    @Override
    public List<AuthorTO> getAuthorList() {
        List<Author> authors = authorMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(authors)){
            return new ArrayList<>();
        }
        List<AuthorTO> authorTOS = new ArrayList<>();
        for (Author author : authors){
            AuthorTO authorTO = new AuthorTO();
            authorTO.setCode(author.getCode());
            authorTO.setName(author.getName());
            authorTOS.add(authorTO);
        }
        return authorTOS;
    }

    @Override
    public List<NovelAuthorVO> getNovelAuthorTable() {
        String url = "http://127.0.0.1:8080/api/getNovelAuthor";
        String response = restTemplateUtil.get(url, String.class, null);
        List<NovelAuthorTO> novelAuthorTOS = JSONArray.parseArray(response, NovelAuthorTO.class);
        if (CollectionUtils.isEmpty(novelAuthorTOS)){
            return new ArrayList<>();
        }
        List<NovelAuthorVO> authorVOS = new ArrayList<>();
        List<Author> authors = authorMapper.selectList(new QueryWrapper<>());
        for (NovelAuthorTO novelAuthorTO : novelAuthorTOS){
            NovelAuthorVO novelAuthorVO = new NovelAuthorVO();
            for (Author author : authors){
                if (novelAuthorTO.getAuthorCode().equalsIgnoreCase(author.getCode())){
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
        author.setId(UUID.randomUUID().toString());
        author.setCode(author.getCode());
        author.setName(author.getName());
        int result=authorMapper.insert(author);
        if (result<0){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateAuthor(Author author) {
        // 第一种全字段更新方法：在代码中将为null的字段附上空字符串的值，注意，null和""不是一个概念
        if(StringUtils.isBlank(author.getName())){
            author.setName("");
        }
        // 第一种方法 end
        return updateById(author);
    }
}
