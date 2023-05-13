package com.letters.letters.services;


import com.letters.letters.DTO.LetterDTO;
import com.letters.letters.DTO.LetterRequestDTO;
import com.letters.letters.Exceptions.InvalidRequestException;
import com.letters.letters.Exceptions.ResourceNotFoundException;
import com.letters.letters.models.LetterRequest;
import com.letters.letters.repositories.LetterRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LetterRequestServiceImpl implements LetterRequestService {



    private final LetterRequestRepository letterRequestRepository ;


    @Override
    @Transactional
    public LetterRequest createLetterRequest(LetterRequestDTO letterRequestDTO) throws InvalidRequestException, IOException {
        LetterRequest letterRequest = new LetterRequest();
        letterRequest.setStudentId(letterRequestDTO.studentId());
        letterRequest.setProfessorId(letterRequestDTO.professorId());
        letterRequest.setLetterType(letterRequestDTO.letterType());
        letterRequest.setCause(letterRequestDTO.cause());
        letterRequest.setReportCard(letterRequestDTO.reportCard().getBytes());
        letterRequest.setCreatedAt(new Date());
        letterRequest.setStatus("Pending");
        letterRequestRepository.save(letterRequest);
        return  letterRequest;
    }




    @Override
    public ResponseEntity<?> getLetterRequestsByStudentId(Long studentId) throws Exception {
        try {
        List<LetterRequest> letterRequests =  letterRequestRepository.findByStudentId(studentId);
        if(!letterRequests.isEmpty()){
            return ResponseEntity.ok(letterRequests);
        }else {
            return ResponseEntity.notFound().build();
        }
        }catch (Exception e){
            throw new Exception("Error getting announcements", e);
        }
    }

    @Override
    public ResponseEntity<List<LetterRequest>> getLetterRequestsByProfessorId(Long professorId) throws Exception {
        try {
        List<LetterRequest> letterRequests =letterRequestRepository.findByProfessorId(professorId);
        if(!letterRequests.isEmpty()){
            return ResponseEntity.ok().body(letterRequests);
        }else {
            return ResponseEntity.notFound().build();
        }
        }catch (Exception e){
            throw new Exception("Error getting announcements", e);
        }
    }

    @Override
    public LetterRequest updateLetterRequest(Long id, LetterRequestDTO letterRequestDTO) throws ResourceNotFoundException, InvalidRequestException, IOException {
        Optional<LetterRequest> existingLetterRequest = letterRequestRepository.findById(id);
        if (existingLetterRequest.isEmpty()) {
            throw new ResourceNotFoundException("Letter request with id " + id + " does not exist");
        }
        if(existingLetterRequest.get().getStatus().equals("Pending")){

            LetterRequest updatedLetterRequest = existingLetterRequest.get();
            updatedLetterRequest.setStudentId(letterRequestDTO.studentId());
            updatedLetterRequest.setProfessorId(letterRequestDTO.professorId());
            updatedLetterRequest.setLetterType(letterRequestDTO.letterType());
            updatedLetterRequest.setCause(letterRequestDTO.cause());
            updatedLetterRequest.setReportCard(letterRequestDTO.reportCard().getBytes());

            return letterRequestRepository.save(updatedLetterRequest);

        }else {
            throw new InvalidRequestException("invalid request");
        }

    }

    @Override
    public LetterRequest letterRequestResponse(Long id, String status) {
        Optional<LetterRequest> existingLetterRequest = letterRequestRepository.findById(id);
        if (existingLetterRequest.isEmpty()) {
            throw new ResourceNotFoundException("Letter request with id " + id + " does not exist");
        }
        LetterRequest updatedLetterRequest = existingLetterRequest.get();
        updatedLetterRequest.setStatus(status);
        return letterRequestRepository.save(updatedLetterRequest);
    }

    @Override
    public LetterRequest createLetter(Long letterRequestId, LetterDTO letterDTO) throws InvalidRequestException, IOException {
        Optional<LetterRequest> existingLetterRequest = letterRequestRepository.findById(letterRequestId);
        if (existingLetterRequest.isEmpty()) {
            throw new ResourceNotFoundException("Letter request with id " + letterRequestId + " does not exist");
        }
        if(existingLetterRequest.get().getStatus().equals("Accepted")){
            LetterRequest updatedLetterRequest = existingLetterRequest.get();
            updatedLetterRequest.setPdfFile(letterDTO.pdfFile().getBytes());
            updatedLetterRequest.setCreatedAt(new Date());
            updatedLetterRequest.setMessage(letterDTO.message());
            return letterRequestRepository.save(updatedLetterRequest);
        }else {
            throw new InvalidRequestException("invalid request");
        }
    }

    @Override
    public void deleteLetterRequest(Long id) throws ResourceNotFoundException {
        Optional<LetterRequest> existingLetterRequest = letterRequestRepository.findById(id);
        if (existingLetterRequest.isEmpty()) {
            throw new ResourceNotFoundException("Letter request with id " + id + " does not exist");
        }
        letterRequestRepository.delete(existingLetterRequest.get());
    }


}

