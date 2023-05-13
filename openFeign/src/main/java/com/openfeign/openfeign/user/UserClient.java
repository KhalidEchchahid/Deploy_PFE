package com.openfeign.openfeign.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("userManagement")
public interface UserClient {

    @GetMapping(path = "api/v1/auth/profiles/students")
    List<String> getStudentBySemester(@RequestParam String semester);

}
