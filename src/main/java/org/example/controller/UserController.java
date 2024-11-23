package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.authorizationvalidators.AuthorizeUserByUsername;
import org.example.dto.requestdto.ChangePasswordRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.metrics.UserRequestMetrics;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@Slf4j
@Tag(name = "UserController")
public class UserController {
    private final UserService userService;
    private final UserRequestMetrics userRequestMetrics;
    private final AuthorizeUserByUsername authorizeUser;

    /**
     * Setting dependencies.
     */

    public UserController(UserService userService,
                          UserRequestMetrics userRequestMetrics,
                          AuthorizeUserByUsername authorizeUser) {
        this.userService = userService;
        this.userRequestMetrics = userRequestMetrics;
        this.authorizeUser = authorizeUser;
    }

    /**
     * GET request to login a user.
     *
     * @param username username of the user
     * @param password password of the user
     */
    @GetMapping("/login")
    @Operation(description = "Logging in a user.")
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully logged in."
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
                        value = "{ \"message\": \"Method is not allowed\" \"httpStatus\": \"405 METHOD_NOT_ALLOWED\" }"
                    )
                )
            ),
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
    public ResponseEntity<ResponseDto<Object>> login(@RequestHeader("username") String username,
                                                     @RequestHeader("password") String password) {
        userRequestMetrics.incrementCounter();
        log.debug("Request to login a user.");
        userService.login(username, password);
        return new ResponseEntity<>(new ResponseDto<>(null, "Successfully logged in."), HttpStatus.OK);
    }

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
    public ResponseEntity<ResponseDto<Object>> changePassword(
        @PathVariable("username") String username,
        @Valid @RequestBody ChangePasswordRequestDto changePasswordDto) {
        userRequestMetrics.incrementCounter();
        log.debug("Request to change password of user with username: {}", username);
        if (!authorizeUser.isAuthorized(username)) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                "You are not authorized to change this password."),
                HttpStatus.FORBIDDEN);
        }
        userService.changeUserPassword(username, changePasswordDto.getPassword(),
            changePasswordDto.getNewPassword());
        return new ResponseEntity<>(
            new ResponseDto<>(null, "Successfully changed user password."), HttpStatus.OK);
    }

}
