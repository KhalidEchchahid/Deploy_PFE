package com.usermanagement.repositories;

import com.usermanagement.models.Profile;
import com.usermanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileRepository extends JpaRepository<Profile , Long> {

    @Transactional
    Profile findByUser(User user);

    void deleteByUser(User user);
}
