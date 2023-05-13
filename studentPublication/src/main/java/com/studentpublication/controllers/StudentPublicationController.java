package com.studentpublication.controllers;

import com.studentpublication.Exeptions.StudentPublicationNotFoundException;
import com.studentpublication.dto.PublicationRequest;
import com.studentpublication.dto.PublicationUpdate;
import com.studentpublication.models.Comment;
import com.studentpublication.services.StudentPublicationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;


@RestController
@RequestMapping("/api/v1/studentPublications")
@AllArgsConstructor
@Slf4j
public class StudentPublicationController {

    private StudentPublicationService studentPublicationService;

    @GetMapping
    public ResponseEntity<?> getAllStudentPublications(@RequestParam(defaultValue = "1") int page) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("application/json")).body(studentPublicationService.getAllStudentPublications(page));
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentPublicationById(@PathVariable Long id) {
        return studentPublicationService.getStudentPublicationById(id);
    }
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getStudentPublicationsByUserId(@PathVariable Long id) throws Exception {
        return studentPublicationService.getStudentPublicationsByUserId(id);
    }
    @PostMapping
    public ResponseEntity<?> createStudentPublication(@ModelAttribute PublicationRequest request) throws IOException {
        return studentPublicationService.createStudentPublication(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteStudentPublicationById(@PathVariable Long id) {
        return studentPublicationService.deleteStudentPublicationById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentPublicationById(@PathVariable Long id, @ModelAttribute PublicationUpdate request) throws IOException {
        return studentPublicationService.updateStudentPublicationById(id, request);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllStudentPublicationsBySearch( @RequestParam("tags") String tags) {
        return studentPublicationService.getAllStudentPublicationsBySearch( tags);
    }

    @PatchMapping("{publicationId}/likes/{userId}")
    public ResponseEntity<?> toggleLikePublication(@PathVariable Long publicationId, @PathVariable Long userId) {
        try {
            return studentPublicationService.toggleLikePublication(publicationId, userId);

        } catch (StudentPublicationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/comments/{publicationId}")
    public ResponseEntity<?> addCommentToPublication(@PathVariable Long publicationId, @RequestBody Comment comment) {
        return studentPublicationService.addCommentToPublication(publicationId, comment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable("commentId") Long commentId ,@RequestParam("userId") Long userId) {
        return studentPublicationService.deleteCommentById(commentId ,userId);
    }
}
