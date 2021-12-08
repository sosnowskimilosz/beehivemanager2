package com.example.behiveemanagerver2.beehive.application;

import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase;
import com.example.behiveemanagerver2.beehive.domain.Beehive;
import com.example.behiveemanagerver2.beehive.domain.MarkOfBeehive;
import com.example.behiveemanagerver2.beehive.domain.MaterialOfBeehive;
import com.example.behiveemanagerver2.beehive.infastructure.MemoryBeehiveRepository;
import com.example.behiveemanagerver2.uploads.application.port.UploadUseCase;
import com.example.behiveemanagerver2.uploads.application.port.UploadUseCase.SaveUploadCommand;
import com.example.behiveemanagerver2.uploads.domain.Upload;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BeehiveService implements BeehiveUseCase {

    private final MemoryBeehiveRepository repository;
    private final UploadUseCase upload;

    @Override
    public List<Beehive> findAll() {
        return repository.findaAll();
    }

    @Override
    public Optional<Beehive> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Beehive> findAccToMark(MarkOfBeehive mark) {
        return repository.findaAll().stream()
                .filter(beehive -> beehive.getMark() == mark)
                .collect(Collectors.toList());
    }

    @Override
    public List<Beehive> findByMaterial(MaterialOfBeehive material) {
        return repository.findaAll()
                .stream()
                .filter(beehive -> beehive.getMaterial() == material)
                .collect(Collectors.toList());
    }

    @Override
    public List<Beehive> findByMark(MarkOfBeehive mark) {
        return repository.findaAll()
                .stream()
                .filter(beehive -> beehive.getMark() == mark)
                .collect(Collectors.toList());
    }

    @Override
    public void updateSymbolOfBeehive(UpdateSymbolOfBeehiveCommand command) {
//        int length = command.getFile().length;
//        System.out.println("Receive cover command: " + command.getFilename() + " bytes: " + length);
        repository.findById(command.getId())
                .ifPresent(beehive -> {
                    Upload savedUpload = upload.save(new SaveUploadCommand(command.getFilename(), command.getFile(), command.getContentType()));
                    beehive.setIdOfSymbolOfBeehive(savedUpload.getId());
                    repository.save(beehive);
                });
    }

    @Override
    public void removeSymbolOfBeehive(Long id) {
        repository.findById(id)
                .ifPresent(beehive -> {
                    if (beehive.getIdOfSymbolOfBeehive() != null) {
                        upload.removeById(beehive.getIdOfSymbolOfBeehive());
                        beehive.setIdOfSymbolOfBeehive(null);
                        repository.save(beehive);
                    }});
    }

    @Override
    public List<Beehive> findByQueenPresence(boolean isQueen) {
        return repository.findaAll().stream()
                .filter(beehive -> beehive.getIsQueen() == isQueen)
                .collect(Collectors.toList());
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }

    @Override
    public Beehive addBeehive(CreateBeehiveCommand command) {
        Beehive beehive = command.toBeehive();
        return repository.save(beehive);
    }

    @Override
    public UpdateBeehiveResponse updateBeehive(UpdateBeehiveCommand command) {
        return repository.findById(command.getId())
                .map(beehive -> {
                    Beehive updatedBeehive = command.updateFields(beehive);
                    repository.save(updatedBeehive);
                    return UpdateBeehiveResponse.SUCCESS;
                }).orElseGet(() -> new UpdateBeehiveResponse(
                        false, Collections.singletonList("Beehive not found with id: " + command.getId())));
    }
}
