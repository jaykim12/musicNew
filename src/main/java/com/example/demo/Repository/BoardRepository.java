package com.example.demo.Repository;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Optional<Board> findBoardById(Long id);
    @Query("select distinct B from Board B left join fetch B.user")
    List<Board> findAll();

    List<Board> findByUser(User user);

    List<Board> findByTitleContaining(String title);

    List<Board> findByUsernameContaining(String username);

    List<Board> findByContentContaining(String content);


}
