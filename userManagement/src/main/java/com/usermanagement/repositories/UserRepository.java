package com.usermanagement.repositories;

import com.usermanagement.dto.ProfessorDTO;
import com.usermanagement.models.Role;
import com.usermanagement.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    Optional<User> findByEmail(String email);
    @Query("SELECT new com.usermanagement.dto.ProfessorDTO(u.id, u.firstName, u.lastName) FROM User u WHERE u.role = 'ROLE_PROFESSOR'")
    List<ProfessorDTO> findProfessors();

    Page<User> findByRole(Role role , Pageable pageable);

    User findByEmailIgnoreCase(String email);

    @Query("SELECT u.email FROM User u WHERE u.role = :role and u.semester = :semester")
    List<String> findEmailsByRoleAndSemester(@Param("role") Role role, @Param("semester") String semester);

}
