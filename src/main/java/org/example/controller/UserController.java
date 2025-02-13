package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.ChangePasswordRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.metrics.UserRequestMetrics;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;
    private final UserRequestMetrics userRequestMetrics;

    /**
     * PUT request to change user password.
     *
     * @param changePasswordDto request contains:
     *                          username(required)
     *                          password(required)
     *                          newPassword(required)
     */
    @PutMapping("/{username}/password")
    @Operation(description = "Change user password.")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully changed the password."
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(
                        value = "{ \"message\": \"Authentication failed\" \"httpStatus\": \"401 UNAUTHORIZED\" }"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "The resource you were trying to reach is not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(
                        value = "{ \"message\": \"Resource not found\" \"httpStatus\": \"404 NOT_FOUND\" }"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "405",
                description = "Method is not allowed.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(
                        value = "{ \"message\": \"Method is not allowed\" "
                            + "\"httpStatus\": \"405 METHOD_NOT_ALLOWED\" }"))),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class),
                    examples = @ExampleObject(
                        value = "{ \"message\": \"Bad request\" \"httpStatus\": \"400 BAD_REQUEST\" }"
                    )
                )
            )
        }
    )
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<ResponseDto<Object>> changePassword(
        @PathVariable("username") String username,
        @Valid @RequestBody ChangePasswordRequestDto changePasswordDto) {
        userRequestMetrics.incrementCounter();
        log.debug("Request to change password of user");
        userService.changeUserPassword(username, changePasswordDto.getPassword(),
            changePasswordDto.getNewPassword());
        return new ResponseEntity<>(
            new ResponseDto<>(null, "Successfully changed user password."), HttpStatus.OK);
    }

}
