package com.example.behiveemanagerver2.beehive.application;

import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase;
import com.example.behiveemanagerver2.beehive.domain.Beehive;
import com.example.behiveemanagerver2.beehive.domain.MarkOfBeehive;
import com.example.behiveemanagerver2.beehive.domain.MaterialOfBeehive;
import com.example.behiveemanagerver2.beehive.infastructure.MemoryBeehiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BeehiveService implements BeehiveUseCase {

    MemoryBeehiveRepository repository;

    @Override
    public List<Beehive> findAll() {
        return repository.findaAll();
    }

    @Override
    public Optional<Beehive> findById(Long id) {
        return Optional.empty();
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
    public void addBeehive(CreateBeehiveCommand command) {
        Beehive beehive = command.toBeehive();
        repository.save(beehive);
    }

    @Override
    public UpdateBeehiveResponse updateBeehive(UpdateBeehiveCommand command) {
        return repository.findById(command.getId())
                .map(beehive ->{
                    Beehive updatedBeehive = command.updateFields(beehive);
                    repository.save(updatedBeehive);
                    return UpdateBeehiveResponse.SUCCESS;
                }).orElseGet(()->new UpdateBeehiveResponse(
                        false, Collections.singletonList("Beehive not found with id: " + command.getId())));
    }
}
