package com.example.behiveemanagerver2.beehive.application.port;

import com.example.behiveemanagerver2.beehive.domain.Beehive;
import com.example.behiveemanagerver2.beehive.domain.MarkOfBeehive;
import com.example.behiveemanagerver2.beehive.domain.MaterialOfBeehive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public interface BeehiveUseCase {

    List<Beehive> findAll();

    Optional<Beehive> findById(Long id);

    List<Beehive> findAccToMark(MarkOfBeehive mark);

    List<Beehive> findByMaterial(MaterialOfBeehive material);

    List<Beehive> findByQueenPresence(boolean isQueen);

    void removeById(Long id);

    Beehive addBeehive(CreateBeehiveCommand command);

    UpdateBeehiveResponse updateBeehive(UpdateBeehiveCommand command);

    List<Beehive> findByMark(MarkOfBeehive valueOf);

    void updateSymbolOfBeehive(UpdateSymbolOfBeehiveCommand command);

    void removeSymbolOfBeehive(Long id);

    @Value
    class UpdateSymbolOfBeehiveCommand {
        Long id;
        byte[] file;
        String contentType;
        String filename;
    }

    @Value
    class CreateBeehiveCommand {
        String symbol;
        MaterialOfBeehive material;
        MarkOfBeehive mark;
        Boolean isQueen;

        public Beehive toBeehive() {
            return new Beehive(symbol, material, mark, isQueen);
        }
    }

    @Value
    class UpdateBeehiveResponse {
        public static UpdateBeehiveResponse SUCCESS = new UpdateBeehiveResponse(true, emptyList());

        boolean success;
        List<String> errors;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateBeehiveCommand {
        Long id;
        String symbol;
        MaterialOfBeehive material;
        MarkOfBeehive mark;
        Boolean isQueen;

        public Beehive updateFields(Beehive beehive) {
            if (symbol != null) {
                beehive.setSymbol(symbol);
            }
            if (material != null) {
                beehive.setMaterial(material);
            }
            if (mark != null) {
                beehive.setMark(mark);
            }
            if (isQueen != null) {
                beehive.setIsQueen(isQueen);
            }
            return beehive;
        }
    }
}
