package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.dto.request.UpdatePasswordRequest;
import com.saathi.saathi_be.domain.dto.request.UpdateUserRequest;
import com.saathi.saathi_be.domain.dto.response.TrustResponse;
import com.saathi.saathi_be.domain.dto.response.UserProfileResponse;
import com.saathi.saathi_be.domain.entity.Place;
import com.saathi.saathi_be.domain.entity.User;
import com.saathi.saathi_be.service.PlaceService;
import com.saathi.saathi_be.service.TestimonialService;
import com.saathi.saathi_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TestimonialService testimonialService;
    private final PlaceService placeService;

    @PutMapping(path = "/update")
    public ResponseEntity<UserProfileResponse> updateUser(
            @Valid @RequestBody UpdateUserRequest request,
            @RequestAttribute UUID userId
    ) {
        User existinguser = userService.getUserById(userId);

        UserProfileResponse updatedUser = userService.updateUser(request, existinguser);
        return ResponseEntity.ok(updatedUser);
    }


    @PostMapping(path = "/update/password")
    public ResponseEntity<?> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request,
            @RequestAttribute UUID userId) {
        try {
            userService.updatePassword(request, userId);
            return ResponseEntity.ok("Password updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while updating the password.");
        }
    }

    @GetMapping(path = "/trust")
    public ResponseEntity<TrustResponse> getData() {
        long reviewers = testimonialService.getReviewers();
        long placesSize = placeService.getPlaceCount();

        return ResponseEntity.ok(TrustResponse.builder()
                        .reviewers(reviewers)
                        .cities(placesSize)
                        .build());
    }
}
