package com.example.behiveemanagerver2.beehive.web;

import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase;
import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase.CreateBeehiveCommand;
import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase.UpdateBeehiveCommand;
import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase.UpdateBeehiveResponse;
import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase.UpdateSymbolOfBeehiveCommand;
import com.example.behiveemanagerver2.beehive.domain.Beehive;
import com.example.behiveemanagerver2.beehive.domain.MarkOfBeehive;
import com.example.behiveemanagerver2.beehive.domain.MaterialOfBeehive;
import com.example.behiveemanagerver2.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequestMapping("/beehive")
@RestController
@AllArgsConstructor
public class BeehiveController {

    private final BeehiveUseCase service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Beehive> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/queen")
    @ResponseStatus(HttpStatus.OK)
    public List<Beehive> getByQueenExistance(@RequestParam boolean isQueen) {
        return service.findByQueenPresence(isQueen);
    }

    @GetMapping("/material")
    @ResponseStatus(HttpStatus.OK)
    public List<Beehive> getByTypeOfMaterial(@RequestParam String material) {
        return service.findByMaterial(MaterialOfBeehive.valueOf(material.toUpperCase()));
    }

    @GetMapping("/mark")
    @ResponseStatus(HttpStatus.OK)
    public List<Beehive> getByMark(@RequestParam String mark) {
        return service.findByMark(MarkOfBeehive.valueOf(mark.toUpperCase()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.removeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addBeehive(@Valid @RequestBody RestBeehiveCommand command) {
        Beehive beehive = service.addBeehive(command.toCreateCommand());
        return ResponseEntity.created(createdBookUri(beehive)).build();
    }

    private URI createdBookUri(Beehive beehive) {
        return new CreatedURI("/" + beehive.getId().toString()).uri();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateBeehive(@PathVariable Long id, @RequestBody RestBeehiveCommand command) {
        UpdateBeehiveResponse response = service.updateBeehive(command.toUpdateCommand(id));
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    @PutMapping("/{id}/symbol")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addSymbolOfBeehive(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("Got file: " + file.getOriginalFilename());
        service.updateSymbolOfBeehive(new UpdateSymbolOfBeehiveCommand(
                id,
                file.getBytes(),
                file.getContentType(),
                file.getOriginalFilename()
        ));
    }

    @Data
    private static class RestBeehiveCommand {
        @NotBlank(message = "Beehive should have a symbol")
        String symbol;

        @NotBlank(message = "Beehive should have a material")
        String material;

        @NotBlank(message = "Beehive should have a mark")
        String mark;

        @NotNull(message = "Data about queen existence is necessary")
        Boolean isQueen;

        CreateBeehiveCommand toCreateCommand() {
            return new CreateBeehiveCommand(symbol, MaterialOfBeehive.valueOf(material), MarkOfBeehive.valueOf(mark), isQueen);
        }

        UpdateBeehiveCommand toUpdateCommand(Long id) {
            return new UpdateBeehiveCommand(id, symbol, MaterialOfBeehive.valueOf(material), MarkOfBeehive.valueOf(mark), isQueen);
        }
    }
}
