package com.hezhan.mybatis.demo.controller;

import com.alibaba.fastjson.JSON;
import com.hezhan.mybatis.demo.entity.po.Author;
import com.hezhan.mybatis.demo.entity.to.AuthorTO;
import com.hezhan.mybatis.demo.entity.vo.NovelAuthorVO;
import com.hezhan.mybatis.demo.service.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author hezhan
 * @Date 2020/3/6 16:47
 */
@Api(value = "自学项目", tags = "mybatis plus3自学项目")
@RestController
@RequestMapping("/api")
public class AuthorController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthorService authorService;

    @ApiOperation(value = "获取作者信息列表", notes = "获取所有作者信息列表")
    @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "失败", response = String.class)})
    @GetMapping("/getAuthors")
    public String getAuthors(@RequestParam(value = "name", required = false) String name){
        logger.debug("开始进入getAuthors方法------------------->");
        List<AuthorTO> authorTOS = authorService.getAuthorList();
        return JSON.toJSONString(authorTOS);
    }

    @ApiOperation(value = "获取作者与小说信息列表", notes = "获取所有作者与小说关联信息列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "失败", response = String.class)})
    @GetMapping("/getNovelAuthorTable")
    public String getTable(){
        List<NovelAuthorVO> authorVOS = authorService.getNovelAuthorTable();
        return JSON.toJSONString(authorVOS);
    }

    @ApiOperation(value = "新增作者信息接口", notes = "新增作者信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "失败", response = String.class)})
    @PostMapping("/author")
    public void insertAuthor(@RequestBody Author author){
        authorService.insertAuthor(author);
    }

    @ApiOperation(value = "新增作者信息接口", notes = "新增作者信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功", response = String.class),
            @ApiResponse(code = 500, message = "失败", response = String.class)})
    @PutMapping("/author")
    public void updateAuthor(@RequestBody Author author){
        authorService.updateAuthor(author);
    }
}
