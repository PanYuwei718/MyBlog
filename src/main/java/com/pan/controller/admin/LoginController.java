package com.pan.controller.admin;

import com.pan.pojo.User;
import com.pan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")  //用于类上,表示类中的所有响应请求的方法都是以该地址作为父路径。
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping    //啥都不加，也行，默认是  上面的 admin
    public String loginPage(){
    return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username,password);
        if (user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }else {
            attributes.addFlashAttribute("message","Username or Password is Incorrect"); //不能用model，model存在当前请求域，重定向后是另一个请求域
            return "redirect:/admin";   //密码错误这种需求就要重定向。
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
