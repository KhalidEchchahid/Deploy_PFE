package com.studentpublication.services;


import com.studentpublication.Exeptions.StudentPublicationNotFoundException;
import com.studentpublication.dto.PublicationRequest;
import com.studentpublication.dto.PublicationUpdate;
import com.studentpublication.models.Comment;
import com.studentpublication.models.Like;
import com.studentpublication.models.StudentPublication;
import com.studentpublication.repositories.CommentRepository;
import com.studentpublication.repositories.LikeRepository;
import com.studentpublication.repositories.StudentPublicationRepository;
import com.studentpublication.util.FileUtils;
import com.studentpublication.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;


import lombok.NoArgsConstructor;
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

public class StudentPublicationServiceImpl implements StudentPublicationService {

    private final StudentPublicationRepository studentPublicationRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

//    private  final JwtUtil jwtUtil;
//
//    private final HttpServletRequest request;
//
//
//
//    public boolean isAuthenticatedUserCreatorOfPublication(Long publicationId) {
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt = authHeader.substring(7);
//
//        String userIdFromToken = jwtUtil.extractUserId(jwt);
//        Long userId = Long.parseLong(userIdFromToken);
//
//        Optional<StudentPublication> studentPublication = studentPublicationRepository.findById(publicationId);
//        if (studentPublication.isPresent()) {
//            return userId.equals(studentPublication.get().getUserId());
//        }
//        return false;
//    }



    @Override
    public ResponseEntity<?> getAllStudentPublications(int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber-1, 6, Sort.by("createdAt").descending());
        Page<StudentPublication> studentPublications = studentPublicationRepository.findAll(pageable);
        for (StudentPublication publication : studentPublications) {
            publication.setPdfFile(FileUtils.decompressFile(publication.getPdfFile()));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("studentPublications", studentPublications.getContent());
        response.put("currentPage", studentPublications.getNumber());
        response.put("totalItems", studentPublications.getTotalElements());
        response.put("totalPages", studentPublications.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getStudentPublicationsByUserId(Long id) throws Exception {

        try {
            List<StudentPublication> studentPublications = studentPublicationRepository.findByUserId(id);
            for (StudentPublication publication : studentPublications) {
                publication.setPdfFile(FileUtils.decompressFile(publication.getPdfFile()));
            }
            return ResponseEntity.ok().body(studentPublications);
        }catch (Exception e){
            throw new Exception("Error getting announcements", e);
        }
    }

    @Override
    public ResponseEntity<?> getStudentPublicationById(Long id) {
        Optional<StudentPublication> studentPublication = studentPublicationRepository.findById(id);
        studentPublication.get().setPdfFile(FileUtils.decompressFile(studentPublication.get().getPdfFile()));
        return studentPublication.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> createStudentPublication(PublicationRequest request) throws IOException {
        StudentPublication studentPublication = new StudentPublication();
        studentPublication.setPdfFile(FileUtils.compressFile(request.pdfFile().getBytes()));
        studentPublication.setCreatedAt(new Date());
        studentPublication.setDescription(request.description());
        List<String> tagList = Arrays.asList(request.tags().split(","));
        Collections.sort(tagList);
        String sortedTags = String.join(",", tagList);
        studentPublication.setTags(sortedTags);
        studentPublication.setTitle(request.title());
        studentPublication.setUserId(request.userId());
        studentPublicationRepository.saveAndFlush(studentPublication) ;
        studentPublication.setPdfFile(FileUtils.decompressFile(studentPublication.getPdfFile()));
        return ResponseEntity.ok(studentPublication);
    }


    @Override
    public ResponseEntity<?> updateStudentPublicationById(Long id, PublicationUpdate request) throws IOException {
        Optional<StudentPublication> existingStudentPublication = studentPublicationRepository.findById(id);
        if (existingStudentPublication.isPresent()) {

            //TODO: Check if the authenticated user is the creator of the announcement

            existingStudentPublication.get().setTitle(request.title());
            existingStudentPublication.get().setDescription(request.description());
            List<String> tagList = Arrays.asList(request.tags().split(","));
            Collections.sort(tagList);
            String sortedTags = String.join(",", tagList);
            existingStudentPublication.get().setTags(sortedTags);
            existingStudentPublication.get().setCreatedAt(new Date());
            studentPublicationRepository.saveAndFlush(existingStudentPublication.get());
            existingStudentPublication.get().setPdfFile(FileUtils.decompressFile(existingStudentPublication.get().getPdfFile()));
            return ResponseEntity.ok(existingStudentPublication.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Long> deleteStudentPublicationById(Long id) {
        Optional<StudentPublication> studentPublication = studentPublicationRepository.findById(id);
        if (studentPublication.isPresent()) {

            studentPublicationRepository.delete(studentPublication.get());

            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> getAllStudentPublicationsBySearch(String tags) {
        try {
            List<String> searchTagList = Arrays.asList(tags.split(","));
            Collections.sort(searchTagList);
            List<StudentPublication> studentPublications = studentPublicationRepository.findAllByOrderByCreatedAtDesc();

            List<StudentPublication> matchedPublications = new ArrayList<>();
            for (StudentPublication publication : studentPublications) {
                List<String> publicationTagList = Arrays.asList(publication.getTags().split(","));
                Collections.sort(publicationTagList);
                if (publication.getTitle().toLowerCase().contains(tags.toLowerCase()) || publicationTagList.containsAll(searchTagList)) {
                    publication.setPdfFile(FileUtils.decompressFile(publication.getPdfFile()));
                    matchedPublications.add(publication);
                }
            }
            return ResponseEntity.ok(matchedPublications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving posts: " + e.getMessage());
        }
    }


    @Override
    public ResponseEntity<?> toggleLikePublication(Long publicationId, Long userId) throws StudentPublicationNotFoundException {
        Optional<StudentPublication> publication = studentPublicationRepository.findById(publicationId);
        if (publication.isPresent()) {
            StudentPublication pub = publication.get();
            Optional<Like> existingLike = pub.getLikes().stream()
                    .filter(like -> like.getUserId().equals(userId))
                    .findFirst();
            if (existingLike.isPresent()) {
                // User has already liked the publication, remove their like
                Like like = existingLike.get();
                pub.getLikes().remove(like);
                likeRepository.delete(like);
            } else {
                // User has not liked the publication yet, create a new like
                Like like = new Like();
                like.setUserId(userId);
                like.setStudentPublication(pub);
                pub.getLikes().add(like);
            }
            studentPublicationRepository.saveAndFlush(pub);
            pub.setPdfFile(FileUtils.decompressFile(pub.getPdfFile()));
            return ResponseEntity.ok(pub);
        } else {
            throw new StudentPublicationNotFoundException("Publication with ID " + publicationId + " not found.");
        }
    }

    @Override
    public ResponseEntity<?> addCommentToPublication(Long publicationId, Comment comment) {
        Optional<StudentPublication> studentPublication = studentPublicationRepository.findById(publicationId);
        comment.setCreatedAt(new Date());
        if (((Optional<?>) studentPublication).isPresent()) {
            StudentPublication publication = studentPublication.get();
            comment.setStudentPublication(publication);
            publication.getComments().add(comment);
            studentPublicationRepository.saveAndFlush(publication);
            publication.setPdfFile(FileUtils.decompressFile(publication.getPdfFile())) ;
            return ResponseEntity.ok(publication);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> deleteCommentById(Long commentId , Long userId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new NotFoundException("Comment not found with id " + commentId));

            StudentPublication studentPublication = comment.getStudentPublication();
            if(userId.equals(comment.getUserId()) || userId.equals(studentPublication.getUserId())){
                studentPublication.getComments().remove(comment);
                studentPublicationRepository.saveAndFlush(studentPublication);
                studentPublication.setPdfFile(FileUtils.decompressFile(studentPublication.getPdfFile())) ;
                return ResponseEntity.ok().body(studentPublication);
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

