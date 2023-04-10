package com.example.demo.repository;

import com.example.demo.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeJpaRepository extends JpaRepository<Node,Long> {
}
