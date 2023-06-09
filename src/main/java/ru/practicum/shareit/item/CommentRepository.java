package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.item.id = ?1")
    List<Comment> findAllCommentsByItem_Id(Long id, Sort sort);

    List<Comment> findAllByItem_IdOrderByCreatedDesc(Long id);

}
