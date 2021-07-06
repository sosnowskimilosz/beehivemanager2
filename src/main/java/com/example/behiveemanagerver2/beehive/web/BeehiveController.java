package com.example.behiveemanagerver2.beehive.web;

import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase;
import com.example.behiveemanagerver2.beehive.domain.Beehive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/beehive")
@RestController
@AllArgsConstructor
public class BeehiveController {

    private final BeehiveUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Beehive> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        service.removeById(id);
    }
}
