package com.usermanagement.dto;

import com.usermanagement.models.Profile;
import com.usermanagement.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private  String token ;
    private Profile profile;
}
