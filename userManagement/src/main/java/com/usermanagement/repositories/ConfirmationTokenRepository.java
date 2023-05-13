package com.usermanagement.repositories;

import com.usermanagement.models.ConfirmationToken;
import com.usermanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByConfirmationToken(String tokenValue);

    void deleteByUser(User user);
}
