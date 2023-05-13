package com.professorarticle.controllers;



import com.professorarticle.Exceptions.ResourceNotFoundException;
import com.professorarticle.dto.PublicationRequest;
import com.professorarticle.models.Comment;

import com.professorarticle.services.ProfessorArticleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/professorArticlesWithAuth")
@AllArgsConstructor
public class ProfessorArticlesWithAuthController {


    private final ProfessorArticleService professorArticleService;

    @PostMapping
    public ResponseEntity<?> createProfessorArticle(@ModelAttribute PublicationRequest request) throws IOException {
        return professorArticleService.createProfessorArticle(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProfessorArticleById(@PathVariable Long id, @Valid @ModelAttribute PublicationRequest request) throws ResourceNotFoundException, IOException {
        return professorArticleService.updateProfessorArticleById(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfessorArticleById(@PathVariable Long id) throws ResourceNotFoundException {
        return professorArticleService.deleteProfessorArticleById(id);
    }


    @PostMapping("/comments/{publicationId}")
    public ResponseEntity<?> addCommentToArticle(@PathVariable Long publicationId, @RequestBody Comment comment) {
        return professorArticleService.addCommentToArticle(publicationId, comment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable("commentId") Long commentId ,@RequestParam("userId") Long userId) {
        return professorArticleService.deleteCommentById(commentId ,userId );
    }

}