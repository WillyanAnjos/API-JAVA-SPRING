package br.com.ditwowin.api.controllers;

import br.com.ditwowin.api.models.Client;
import br.com.ditwowin.api.services.ClientService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1/client")
@CrossOrigin
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("create")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> createNewClient (@RequestBody @Valid Client client) {
        if (clientService.create(client)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>("CPF already in use",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("delete/{cpf}")
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> deleteClient (@PathVariable String cpf) {
        if (clientService.delete(cpf)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>("CPF not found", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}