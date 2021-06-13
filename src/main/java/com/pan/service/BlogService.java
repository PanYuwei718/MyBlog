package com.pan.service;

import com.pan.pojo.Blog;
import com.pan.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);  //全局搜索按钮查询

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    List<Blog> listRecommendTop(Integer size);

    Blog getAndConvert(Long id);


    Map<String, List<Blog>> archiveBlog();

    Long countBlog();





}
