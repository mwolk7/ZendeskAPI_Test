package com.example.demo.service;

import com.example.demo.RequestResponseLoggingInterceptor;
import com.example.demo.dto.Ticket;
import com.example.demo.response.ComentarioResponseDTO;
import com.example.demo.response.TicketResponseDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;

@Service
public class TicketService {

    private static Logger LOG = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Value("${url}")
    private String url;

    @Value("${usuario}")
    private String usuario;

    @Value("${pw}")
    private String pw;

    public TicketResponseDTO crearTicket(Ticket ticketDTO) {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        LOG.info(ticketDTO.toString());
        HttpEntity httpEntity = new HttpEntity<>(ticketDTO, createHeaders(usuario, pw));
        TicketResponseDTO response = restTemplate.postForObject(url, httpEntity, TicketResponseDTO.class);

        return response;
    }

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    public ComentarioResponseDTO listarTickets(int ticketId) {
        String url = "https://teclab1554150453.zendesk.com/api/v2/tickets/" + ticketId + "/comments.json";
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        LOG.info(String.valueOf(ticketId));
        HttpHeaders httpHeaders = new HttpHeaders(createHeaders(usuario, pw));
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<ComentarioResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, ComentarioResponseDTO.class);
        ComentarioResponseDTO comentarioResponseDTO = new ComentarioResponseDTO();
        comentarioResponseDTO = validate(response, comentarioResponseDTO);

        return comentarioResponseDTO;
    }

    private ComentarioResponseDTO validate(ResponseEntity<ComentarioResponseDTO> response, ComentarioResponseDTO comentarioResponseDTO) {

        if (response.getBody() != null && response.getStatusCode().is2xxSuccessful()) {
            comentarioResponseDTO = response.getBody();
            comentarioResponseDTO.setStatusCode(HttpStatus.OK);
            comentarioResponseDTO.setMessage("200");

        } else if (response.getStatusCode().is5xxServerError()) {
            comentarioResponseDTO.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
            comentarioResponseDTO.setMessage("500");

        } else {
            comentarioResponseDTO.setStatusCode(HttpStatus.BAD_REQUEST);
            comentarioResponseDTO.setMessage("400");
        }
        return comentarioResponseDTO;
    }

    public void borrarTicket(int id) {

        String url = "https://teclab1554150453.zendesk.com/api/v2/tickets/" + id + ".json";

        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        HttpHeaders httpHeaders = new HttpHeaders(createHeaders(usuario, pw));
        HttpEntity entity = new HttpEntity(httpHeaders);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, ComentarioResponseDTO.class);
    }
}
