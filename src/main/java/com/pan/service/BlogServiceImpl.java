package com.pan.service;

import com.pan.NotFoundException;
import com.pan.dao.BlogRepository;
import com.pan.pojo.Blog;
import com.pan.pojo.Type;
import com.pan.utils.MarkDownUtils;
import com.pan.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {

            @Override    //root代表查询的对象， CriteriaBuilder设置一些表达式
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                if (blog.getTitle() != null && !"".equals(blog.getTitle())){
                    predicateList.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null){
                    predicateList.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){   //查询的时候是否指定了推荐？
                    predicateList.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicateList.toArray(new Predicate[predicateList.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {  //全局搜索按钮查询

        return blogRepository.findByQuery(query,pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){
            blog.setCreatTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null){
            throw new NotFoundException("No exist");
        }
        Date CreteTime = b.getCreatTime();  //提前保存b中已经有的
        Integer views = b.getViews();
        BeanUtils.copyProperties(blog,b);   //前端修改的博客属性，全部拷贝到b  （mybatis可以直接用copy这步，自动跳过为null的）
        b.setCreatTime(CreteTime);
        b.setViews(views);
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }


    @Override
    public List<Blog> listRecommendTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null){
            throw new NotFoundException("No this blog");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkDownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);  //这个方法是点进具体博客调用的，所以更新浏览次数写在这，直接+1
        return b;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> times = blogRepository.findGroupTime();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String time : times){
            map.put(time,blogRepository.findByTime(time));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
