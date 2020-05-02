package com.example.demo.controller;


import com.example.demo.dto.Ticket;
import com.example.demo.response.ComentarioResponseDTO;
import com.example.demo.response.TicketResponseDTO;
import com.example.demo.service.TicketService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TicketController {

    private static final Log LOG = LogFactory.getLog(TicketController.class);

    @Autowired
    protected TicketService ticketService;

//    @GetMapping("/ticket/test")
//    public TicketDTO test () {
//        ComentarioDTO comentarioDTO = new ComentarioDTO("Este es el body del comentarioDTO");
//        TicketDTO ticketDTO = new TicketDTO("este es el subject" , comentarioDTO, "este es el priority");
//        return ticketDTO;
//    }

    @PostMapping(value = "/ticket/crear", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public TicketResponseDTO crear(@RequestBody Ticket ticket) {
        return ticketService.crearTicket(ticket);

    }

    @GetMapping("/ticket/listar/{id}")
    @ResponseBody
    public ComentarioResponseDTO listar(@PathVariable int id) {
        return ticketService.listarTickets(id);
    }

    @DeleteMapping("/ticket/borrar/{id}")
    public void borrar(@PathVariable int id) {
         ticketService.borrarTicket(id);
    }
}
