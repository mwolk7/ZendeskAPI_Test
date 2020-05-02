package com.example.demo.dto;

import com.example.demo.request.ComentarioRequestDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketDTO {

    private String subject;
    @JsonProperty(value = "comment")
    private ComentarioRequestDTO comentarioDTO;
    private String priority;
}
