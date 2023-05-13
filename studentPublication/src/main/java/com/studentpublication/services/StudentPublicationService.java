package com.studentpublication.services;

import com.studentpublication.Exeptions.StudentPublicationNotFoundException;
import com.studentpublication.dto.PublicationRequest;
import com.studentpublication.dto.PublicationUpdate;
import com.studentpublication.models.Comment;
import com.studentpublication.models.StudentPublication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface StudentPublicationService {
    ResponseEntity<?> getAllStudentPublications(int pageNumber);
    ResponseEntity<?> getStudentPublicationsByUserId(Long id) throws Exception;

    ResponseEntity<?> getStudentPublicationById(Long id);

    ResponseEntity<?> createStudentPublication(PublicationRequest request ) throws IOException;

    ResponseEntity<?> updateStudentPublicationById(Long id, PublicationUpdate request) throws IOException;

    ResponseEntity<Long> deleteStudentPublicationById(Long id);

    public ResponseEntity<?> getAllStudentPublicationsBySearch(String tags);

    public ResponseEntity<?> toggleLikePublication(Long publicationId, Long userId) throws StudentPublicationNotFoundException;

    ResponseEntity<?> addCommentToPublication(Long publicationId, Comment comment);

    ResponseEntity<?> deleteCommentById(Long commentId ,Long userId);

}
