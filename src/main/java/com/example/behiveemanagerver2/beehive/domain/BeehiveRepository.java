package com.example.behiveemanagerver2.beehive.domain;

import java.util.List;
import java.util.Optional;

public interface BeehiveRepository {

    List<Beehive> findaAll();

    Beehive save(Beehive beehive);

    Optional<Beehive> findById(Long id);

    void removeById(Long id);
}
