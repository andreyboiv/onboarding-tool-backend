package com.boivalenko.businessapp.onboarding.web.app.repository;

import com.boivalenko.businessapp.onboarding.web.app.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEmployeesToTaskLoginOrderByIdAsc(String login);
    List<Task> findByEmployeesToTaskEmailOrderByTitleAsc(String email);

    @Query("SELECT c FROM Task c where " +
            "(:title is null or :title='' " +
            " or lower(c.title) like lower(concat('%', :title,'%'))) " +
            " and c.employeesToTask.email=:email  " +
            " order by c.title asc")
    List<Task> findAllByEmailQuery(@Param("title") String title,
                                   @Param("email") String email);


    @Query("SELECT t FROM Task t where " +
            "(:title is null or :title='' or lower(t.title) like lower(concat('%', :title,'%'))) and " +
            "(:completed is null or t.completed=:completed) and " +
            "(:categoryId is null or t.category.id=:categoryId) and " +
            "(cast(:dateFrom as date) is null or t.taskDate>=:dateFrom) and " +
            "(cast(:dateTo as date) is null or t.taskDate<=:dateTo) and " +
            "(t.employeesToTask.email=:email)"
    )
    Page<Task> findAllByParams(
            @Param("title") String title,
                            @Param("completed") Boolean completed,
                            @Param("categoryId") Long categoryId,
                            @Param("email") String email,
                            @Param("dateFrom") Date dateFrom,
                            @Param("dateTo") Date dateTo,
                            Pageable pageable
    );

    List<Task> findByCategoryIdOrderByIdAsc(Long id);

    boolean existsAllByCategoryId(Long id);
}
