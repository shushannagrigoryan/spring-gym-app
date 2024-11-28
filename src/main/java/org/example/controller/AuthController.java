//package org.example.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import lombok.extern.slf4j.Slf4j;
//import org.example.dto.requestdto.RefreshTokenRequestDto;
//import org.example.dto.responsedto.JwtAuthenticationResponse;
//import org.example.services.RefreshTokenService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("auth")
//@Slf4j
//public class AuthController {
//    private final RefreshTokenService refreshTokenService;
//
//    public AuthController(RefreshTokenService refreshTokenService) {
//        this.refreshTokenService = refreshTokenService;
//    }
//
//    /**
//     * Generates a new access token from the given refresh token.
//     *
//     * @param refreshTokenRequestDto {@code RefreshTokenRequestDto} refresh token
//     * @return {@code ResponseEntity<JwtAuthenticationResponse>} the generated access token
//     */
//
//    @Operation(description = "Generating a new access token from the given refresh token.")
//    @ApiResponses(
//        {
//            @ApiResponse(
//                responseCode = "200",
//                description = "Successfully created a new access token.",
//                content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = ResponseEntity.class)
//                )
//            ),
//            @ApiResponse(
//                responseCode = "401",
//                description = "The request is unauthenticated.",
//                content = @Content(
//                    mediaType = "application/json"
//                )
//            )
//        }
//    )
//    @PostMapping("/refreshToken")
//    public ResponseEntity<JwtAuthenticationResponse> refreshAccessToken(
//        @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
//        log.debug("Request to generate a new access token using the given refresh token.");
//        return new ResponseEntity<>(new JwtAuthenticationResponse(
//            refreshTokenService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken())),
//            HttpStatus.OK);
//    }
//}
