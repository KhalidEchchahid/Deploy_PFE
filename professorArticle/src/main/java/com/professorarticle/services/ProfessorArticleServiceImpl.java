package com.professorarticle.services;

import com.professorarticle.Exceptions.ProfessorArticleNotFoundException;
import com.professorarticle.Exceptions.ResourceNotFoundException;
import com.professorarticle.dto.PublicationRequest;
import com.professorarticle.models.Comment;
import com.professorarticle.models.ProfessorArticle;
import com.professorarticle.repositories.CommentRepository;
import com.professorarticle.repositories.ProfessorArticleRepository;
import com.professorarticle.util.FileUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class ProfessorArticleServiceImpl implements ProfessorArticleService {


    private final ProfessorArticleRepository professorArticleRepository;
    private final CommentRepository commentRepository;

    @Override
    public ResponseEntity<?> getAllProfessorArticles() {

        List<ProfessorArticle> professorArticlePage = professorArticleRepository.findAll();
        Collections.reverse(professorArticlePage);
        for (ProfessorArticle publication : professorArticlePage) {
            publication.setImageData(FileUtils.decompressImage(publication.getImageData()));
        }
        return ResponseEntity.ok(professorArticlePage);
    }


    @Override
    public List<String> getAllCategories() {
        List<String> categories = professorArticleRepository.findAllDistinctCategories();
        return categories;
    }

    @Override
    public List<ProfessorArticle> getRandomArticlesByCategory() {

        List<String> categories = professorArticleRepository.findAllDistinctCategories();

        List< ProfessorArticle> randomArticlesByCategory = new ArrayList<ProfessorArticle>();

        for (String category : categories) {
            List<ProfessorArticle> articles = professorArticleRepository.findByCategory(category);

            if (!articles.isEmpty()) {
                ProfessorArticle randomArticle = articles.get(new Random().nextInt(articles.size()));
                randomArticle.setImageData(FileUtils.decompressImage(randomArticle.getImageData()));
                randomArticlesByCategory.add(randomArticle);
            }
        }

        return randomArticlesByCategory;
    }

    @Override
    public List<ProfessorArticle> getAllArticlesByCategory(String category) {
        List< ProfessorArticle> ArticlesByCategory = professorArticleRepository.findByCategory(category);
        Collections.reverse(ArticlesByCategory);
        for (ProfessorArticle article : ArticlesByCategory) {
            article.setImageData(FileUtils.decompressImage(article.getImageData()));
        }
        return ArticlesByCategory;
    }

    @Override
    public List<ProfessorArticle> getAllArticlesByUserId(Long id) throws Exception {
        try {
            List<ProfessorArticle> professorArticles = professorArticleRepository.findByUserId(id);
            for (ProfessorArticle article : professorArticles) {
                article.setImageData(FileUtils.decompressImage(article.getImageData()));
            }
            return professorArticles;
        }catch (Exception e){
            throw new Exception("Error getting announcements", e);
        }
    }


    @Override
    public ResponseEntity<?> getProfessorArticleById(Long id) throws ResourceNotFoundException {
        Optional<ProfessorArticle> professorArticleOptional = professorArticleRepository.findById(id);
        professorArticleOptional.get().setImageData(FileUtils.decompressImage(professorArticleOptional.get().getImageData()));
        if (professorArticleOptional.isPresent()) {
            return ResponseEntity.ok(professorArticleOptional.get());
        } else {
            throw new ResourceNotFoundException("ProfessorArticle", "id", id);
        }
    }

    @Override
    public ResponseEntity<?> getArticlesBySearch(String title) {
        List<ProfessorArticle> list = professorArticleRepository.findByTitleContainingIgnoreCase(title);
        if(!list.isEmpty()){


        for (ProfessorArticle article : list) {
            article.setImageData(FileUtils.decompressImage(article.getImageData()));
        }
        return  ResponseEntity.ok(list);
        }else {
            return ResponseEntity.notFound().build() ;
        }
    }

    @Override
    public ResponseEntity<?> createProfessorArticle(PublicationRequest request) throws IOException {

            ProfessorArticle professorArticle = new ProfessorArticle();
            professorArticle.setImageData(FileUtils.compressImage(request.imageFile().getBytes()));
            professorArticle.setCreatedAt(new Date());
            professorArticle.setContent(request.content());
            professorArticle.setCategory(request.category());
            professorArticle.setTitle(request.title());
            professorArticle.setUserId(request.userId());
            ProfessorArticle savedProfessorArticle = professorArticleRepository.saveAndFlush(professorArticle);
            savedProfessorArticle.setImageData(FileUtils.decompressImage(savedProfessorArticle.getImageData()));
            return ResponseEntity.ok(savedProfessorArticle);
    }

    @Override
    public ResponseEntity<String> updateProfessorArticleById(Long id, PublicationRequest request) throws ResourceNotFoundException, IOException {
        Optional<ProfessorArticle> professorArticleOptional = professorArticleRepository.findById(id);
        if (professorArticleOptional.isPresent()) {

            //TODO: Check if the authenticated user is the creator of the announcement

            ProfessorArticle existingProfessorArticle = professorArticleOptional.get();
            existingProfessorArticle.setTitle(request.title());
            existingProfessorArticle.setContent(request.content());
            existingProfessorArticle.setCategory(request.category());
            existingProfessorArticle.setImageData(FileUtils.compressImage(request.imageFile().getBytes()));
            professorArticleRepository.save(existingProfessorArticle);
            return ResponseEntity.ok("ProfessorArticle with id " + id + " updated successfully");
        } else {
            throw new ResourceNotFoundException("ProfessorArticle", "id", id);
        }
    }

    @Override
    public ResponseEntity<String> deleteProfessorArticleById(Long id) throws ResourceNotFoundException {
        Optional<ProfessorArticle> professorArticleOptional = professorArticleRepository.findById(id);
        if (professorArticleOptional.isPresent()) {

            //TODO: Check if the authenticated user is the creator of the announcement

            ProfessorArticle professorArticle = professorArticleOptional.get();
            professorArticleRepository.delete(professorArticle);
            return ResponseEntity.ok("ProfessorArticle with id " + id + " deleted successfully");
        } else {
            throw new ResourceNotFoundException("ProfessorArticle", "id", id);
        }
    }



    @Override
    public ResponseEntity<?> addCommentToArticle(Long publicationId, Comment comment) {
        Optional<ProfessorArticle> studentPublication = professorArticleRepository.findById(publicationId);
        comment.setCreatedAt(new Date());
        if (((Optional<?>) studentPublication).isPresent()) {
            ProfessorArticle article = studentPublication.get();
            comment.setProfessorArticle(article);
            article.getComments().add(comment);
            professorArticleRepository.saveAndFlush(article);
            article.setImageData(FileUtils.decompressImage(article.getImageData()));
            return ResponseEntity.ok(article);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> deleteCommentById(Long commentId , Long userId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new NotFoundException("Comment not found with id " + commentId));

            ProfessorArticle professorArticle = comment.getProfessorArticle();
            if(userId.equals(comment.getUserId()) || userId.equals(professorArticle.getUserId())){
                professorArticle.getComments().remove(comment);
                professorArticleRepository.saveAndFlush(professorArticle);
                professorArticle.setImageData(FileUtils.decompressImage(professorArticle.getImageData())); ;
                return ResponseEntity.ok().body(professorArticle);
            }else {
                return ResponseEntity.ok().body("Comment with id " + commentId + " can't be deleted.");
            }
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}

