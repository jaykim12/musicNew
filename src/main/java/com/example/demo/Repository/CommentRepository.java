package com.example.demo.Repository;

import com.example.demo.Entity.Comment;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findById(Long id);

    List<Comment> findByUser(User user);


}
