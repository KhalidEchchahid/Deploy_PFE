package com.professorarticle.services;

import com.professorarticle.Exceptions.ProfessorArticleNotFoundException;
import com.professorarticle.Exceptions.ResourceNotFoundException;
import com.professorarticle.dto.PublicationRequest;
import com.professorarticle.models.Comment;
import com.professorarticle.models.ProfessorArticle;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProfessorArticleService {
    ResponseEntity<?> getAllProfessorArticles();
     List<String> getAllCategories();
     List<ProfessorArticle> getRandomArticlesByCategory();
     List<ProfessorArticle> getAllArticlesByCategory(String category);
     List<ProfessorArticle> getAllArticlesByUserId( Long id) throws Exception;

    ResponseEntity<?> getProfessorArticleById(Long id) throws ResourceNotFoundException;

    ResponseEntity<?> getArticlesBySearch(String title);

    ResponseEntity<?> createProfessorArticle(PublicationRequest request) throws IOException;

    ResponseEntity<String> updateProfessorArticleById(Long id, PublicationRequest request) throws ResourceNotFoundException, IOException;

    ResponseEntity<String> deleteProfessorArticleById(Long id) throws ResourceNotFoundException;


    ResponseEntity<?> addCommentToArticle(Long publicationId, Comment comment);

    ResponseEntity<?> deleteCommentById(Long commentId , Long userId);
}
