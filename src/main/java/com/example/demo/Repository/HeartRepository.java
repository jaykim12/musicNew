package com.example.demo.Repository;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Heart;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart,Long> {
    Optional<Heart> findByBoardAndUser(Board board, User user);
    Long countByBoard(Board board);
}
