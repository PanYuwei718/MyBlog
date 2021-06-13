package com.pan.service;
import com.pan.pojo.Type;
import jdk.nashorn.internal.ir.IdentNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TypeService {

    // 增
     Type saveType(Type type);
    // 删
     void deleteType(Long id);
    // 改
     Type updateType(Long id,Type type);
    // 查
     Type getType(Long id);

    //分页
     Page<Type> listType(Pageable pageable);

    //按名称查找
     Type findTypeByName(String name);

     List<Type> listType();

     List<Type> listTypeTop(Integer size);




}
