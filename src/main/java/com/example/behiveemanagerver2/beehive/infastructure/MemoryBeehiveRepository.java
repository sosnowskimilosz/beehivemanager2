package com.example.behiveemanagerver2.beehive.infastructure;

import com.example.behiveemanagerver2.beehive.domain.Beehive;
import com.example.behiveemanagerver2.beehive.domain.BeehiveRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryBeehiveRepository implements BeehiveRepository {

    private final Map<Long, Beehive> storage = new HashMap<>();
    private final AtomicLong ID_VALUE = new AtomicLong(0L);

    @Override
    public List<Beehive> findaAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Beehive save(Beehive beehive) {
        if (beehive.getId() != null) {
            storage.put(beehive.getId(), beehive);
        } else {
            Long nextId = nextId();
            beehive.setId(nextId);
            storage.put(nextId, beehive);
        }
        return beehive;
    }

    private Long nextId() {
        return ID_VALUE.getAndIncrement();
    }

    @Override
    public Optional<Beehive> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(Long id) {
        storage.remove(id);
    }
}
