package com.professorarticle.repositories;

import com.professorarticle.models.ProfessorArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorArticleRepository extends JpaRepository<ProfessorArticle , Long> {

    @Query("SELECT DISTINCT p.category FROM ProfessorArticle p")
    List<String> findAllDistinctCategories();

    @Transactional
    List<ProfessorArticle> findByCategory(String category);

    @Transactional
    List<ProfessorArticle> findByTitleContainingIgnoreCase(String title);

    @Transactional
    List<ProfessorArticle> findByUserId(Long id);
}
