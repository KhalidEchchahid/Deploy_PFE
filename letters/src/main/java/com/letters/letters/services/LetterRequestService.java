package com.letters.letters.services;


import com.letters.letters.DTO.LetterDTO;
import com.letters.letters.DTO.LetterRequestDTO;
import com.letters.letters.Exceptions.InvalidRequestException;
import com.letters.letters.Exceptions.ResourceNotFoundException;
import com.letters.letters.models.LetterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface LetterRequestService {


    @Transactional
    LetterRequest createLetterRequest(LetterRequestDTO letterRequestDTO) throws InvalidRequestException, IOException;



    ResponseEntity<?>  getLetterRequestsByStudentId(Long studentId) throws Exception;

    ResponseEntity<List<LetterRequest>> getLetterRequestsByProfessorId(Long professorId) throws Exception;

    LetterRequest updateLetterRequest(Long id, LetterRequestDTO letterRequestDTO) throws ResourceNotFoundException, InvalidRequestException, IOException;

    void deleteLetterRequest(Long id) throws ResourceNotFoundException;

    LetterRequest letterRequestResponse(Long id, String status);


    LetterRequest createLetter(Long letterRequestId, LetterDTO letterDTO) throws InvalidRequestException, IOException;

}
