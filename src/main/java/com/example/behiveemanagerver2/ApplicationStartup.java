package com.example.behiveemanagerver2;

import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase;
import com.example.behiveemanagerver2.beehive.application.port.BeehiveUseCase.CreateBeehiveCommand;
import com.example.behiveemanagerver2.beehive.domain.Beehive;
import com.example.behiveemanagerver2.beehive.domain.MarkOfBeehive;
import com.example.behiveemanagerver2.beehive.domain.MaterialOfBeehive;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApplicationStartup implements CommandLineRunner {


    private final BeehiveUseCase service;

    @Override
    public void run(String... args) throws Exception {
        initData();
        System.out.println(service.findAll());
        System.out.println(service.findById(0L));
    }

    private void initData() {
        service.addBeehive(new CreateBeehiveCommand("01", MaterialOfBeehive.WOOD, MarkOfBeehive.GOOD, true));
        service.addBeehive(new CreateBeehiveCommand("02", MaterialOfBeehive.POLYURETHANE, MarkOfBeehive.VERY_WELL, true));
        service.addBeehive(new CreateBeehiveCommand("03", MaterialOfBeehive.STYRODUR, MarkOfBeehive.BAD, false));
    }
}
