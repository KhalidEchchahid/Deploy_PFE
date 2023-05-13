package com.studentpublication.repositories;

import com.studentpublication.models.Comment;
import com.studentpublication.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
