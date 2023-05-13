package com.professorarticle.controllers;


import com.professorarticle.Exceptions.ResourceNotFoundException;
import com.professorarticle.dto.PublicationRequest;
import com.professorarticle.models.Comment;
import com.professorarticle.models.ProfessorArticle;
import com.professorarticle.services.ProfessorArticleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/professorArticles")
@AllArgsConstructor
public class ProfessorArticleController {


    private final ProfessorArticleService professorArticleService;

    @GetMapping
    public ResponseEntity<?> getAllProfessorArticles() {
        return professorArticleService.getAllProfessorArticles();
    }
    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return professorArticleService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessorArticleById(@PathVariable Long id) throws ResourceNotFoundException {
        return professorArticleService.getProfessorArticleById(id);
    }

    @GetMapping("/profile/{id}")
    public List<ProfessorArticle> getProfessorArticleByUserId(@PathVariable Long id) throws  Exception {
        return  professorArticleService.getAllArticlesByUserId(id);
    }

    @GetMapping("/random")
    public List<ProfessorArticle> getRandomArticlesByCategory(){
        return professorArticleService.getRandomArticlesByCategory();
    }

    @GetMapping("/category/{category}")
    public List<ProfessorArticle> getArticlesByCategory(@PathVariable String category){
        return professorArticleService.getAllArticlesByCategory(category);
    }
    @GetMapping("/search")
    public ResponseEntity<?> getArticlesBySearch(@RequestParam("title") String title) {

        return  professorArticleService.getArticlesBySearch(title);
    }


}

