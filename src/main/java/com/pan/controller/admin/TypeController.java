package com.pan.controller.admin;

import com.pan.pojo.Type;
import com.pan.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/type_input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type,BindingResult result,RedirectAttributes attributes){
        Type type1 = typeService.findTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","can not repetition");
        }

        if (result.hasErrors()){
            return "admin/type_input";
        }
        Type t = typeService.saveType(type);
        if (t == null){
            attributes.addFlashAttribute("message","defeat");
        }else{
            attributes.addFlashAttribute("message","success!");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/input")   //编辑类别（修改已存在类别的名称）
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/type_input";
    }

    @PostMapping("/types/{id}")   //编辑后是更新，还要各种判断
    public String editPost(@Valid Type type,BindingResult result,RedirectAttributes attributes,@PathVariable Long id){
        Type type1 = typeService.findTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","can not repetition");
        }

        if (result.hasErrors()){
            return "admin/type_input";
        }
        Type t = typeService.updateType(id,type);
        if (t == null){
            attributes.addFlashAttribute("message","Edit Defeat");
        }else{
            attributes.addFlashAttribute("message","Edit Success!");
        }
        return "redirect:/admin/types";
    }

  @GetMapping("/types/{id}/delete")   //删除type
  public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","Delete Success!");
        return "redirect:/admin/types";
  }

}
