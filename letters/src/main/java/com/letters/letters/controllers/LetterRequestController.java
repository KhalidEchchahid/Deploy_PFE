package com.letters.letters.controllers;


import com.letters.letters.DTO.LetterDTO;
import com.letters.letters.DTO.LetterRequestDTO;
import com.letters.letters.Exceptions.InvalidRequestException;
import com.letters.letters.Exceptions.ResourceNotFoundException;
import com.letters.letters.models.LetterRequest;
import com.letters.letters.services.LetterRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/letter_request")
@AllArgsConstructor
public class LetterRequestController {

    private final LetterRequestService letterRequestService;


    @PostMapping
    public ResponseEntity<LetterRequest> createLetterRequest(@ModelAttribute LetterRequestDTO letterRequestDTO) {
        try {
            LetterRequest createdLetterRequest = letterRequestService.createLetterRequest(letterRequestDTO);
            return ResponseEntity.ok(createdLetterRequest);
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getLetterRequestsByStudentId(@PathVariable Long studentId) throws Exception {

        return ResponseEntity.ok(letterRequestService.getLetterRequestsByStudentId(studentId).getBody());
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<LetterRequest>> getLetterRequestsByProfessorId(@PathVariable Long professorId) throws Exception {

        return ResponseEntity.ok(letterRequestService.getLetterRequestsByProfessorId(professorId).getBody());
    }


    @PutMapping("/{id}")
    public ResponseEntity<LetterRequest> updateLetterRequest(@PathVariable Long id, @ModelAttribute LetterRequestDTO letterRequestDTO) {
        try {

            return ResponseEntity.ok(letterRequestService.updateLetterRequest(id, letterRequestDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLetterRequest(@PathVariable Long id) {
        try {
            letterRequestService.deleteLetterRequest(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/response/{id}")
    public ResponseEntity<LetterRequest> letterRequestResponse(@PathVariable Long id, @RequestParam String status) {
        try {

            return ResponseEntity.ok(letterRequestService.letterRequestResponse(id, status));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }




    @PutMapping("/letter/{letterRequestId}")
    public ResponseEntity<LetterRequest> createLetter(@PathVariable Long letterRequestId, @ModelAttribute LetterDTO letterDTO) {
        try {
            LetterRequest letter = letterRequestService.createLetter(letterRequestId, letterDTO);
            return ResponseEntity.ok(letter);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
