package com.example.demo.response;

import com.example.demo.dto.ComentarioDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ComentarioResponseDTO {

    @JsonProperty(value = "comments")
    private List<ComentarioDTO> comments;
    private HttpStatus statusCode;
    private String message;

}
