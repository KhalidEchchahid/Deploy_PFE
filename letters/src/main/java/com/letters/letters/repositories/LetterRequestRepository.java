package com.letters.letters.repositories;


import com.letters.letters.models.LetterRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Repository
public interface LetterRequestRepository extends JpaRepository<LetterRequest, Long> {

    @Transactional
    List<LetterRequest> findByStudentId(Long id);

    @Transactional
    List<LetterRequest> findByProfessorId(Long id);
}
