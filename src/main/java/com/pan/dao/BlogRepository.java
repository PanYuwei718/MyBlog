package com.pan.dao;

import com.pan.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor {
            //用JpaSpecificationExecutor实现动态组合查询



    @Query("SELECT b FROM Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    @Query("select function('date_format',b.updateTime,'%Y-%m') from Blog b group by function('date_format',b.updateTime,'%Y-%m') order by function('date_format',b.updateTime,'%Y-%m') desc")
    List<String> findGroupTime();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y-%m') = ?1")
    List<Blog> findByTime(String time);
}
