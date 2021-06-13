package com.pan.controller.admin;


import com.pan.pojo.Blog;
import com.pan.pojo.User;
import com.pan.service.BlogService;
import com.pan.service.TypeService;
import com.pan.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {


    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model, BlogQuery blog){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable,blog));
            return "admin/blogs";
    }


    @PostMapping("/blogs/search")  //ajax load 方法只能post？。。
    public String blogPageSearch(@PageableDefault(size = 2,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                             Pageable pageable, BlogQuery blog, Model model){

        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    @GetMapping("blogs/input")
    public String input(Model model){
        model.addAttribute("types",typeService.listType());  //初始化分类
        model.addAttribute("blog",new Blog());    //初始化，如果是edit 直接拿内容
        return "admin/blogs-input";
    }

    @GetMapping("blogs/{id}/input")             //博客修改
    public String editinput(@PathVariable Long id, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("blog",blogService.getBlog(id));
        return "admin/blogs-input";
    }


    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        Blog b;
        if (blog.getId() == null){
            b = blogService.saveBlog(blog);
        }else{
            b = blogService.updateBlog(blog.getId(),blog);
        }

        if (b == null){
            attributes.addFlashAttribute("message","defeat!");
        }else{
            attributes.addFlashAttribute("message","success!");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","delete success!");
        return "redirect:/admin/blogs";
    }



}

