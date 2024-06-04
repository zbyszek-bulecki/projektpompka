package com.sharks.gardenManager.service;

import com.sharks.gardenManager.DTO.CommandsRequesterWithTimestampDTO;
import com.sharks.gardenManager.DTO.SettingsDTO;
import com.sharks.gardenManager.TestContainersBase;
import com.sharks.gardenManager.entities.Planter;
import com.sharks.gardenManager.entities.PlanterSettings;
import com.sharks.gardenManager.repositories.PlanterRepository;
import com.sharks.gardenManager.repositories.PlanterSettingsRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;


@SpringBootTest
@Transactional
class SettingsServiceTest extends TestContainersBase {

    @Autowired
    private PlanterSettingsRepository planterSettingsRepository;

    @Autowired
    private PlanterRepository planterRepository;

    private SettingsService objectUnderTest;

    @BeforeEach
    void setUp() {
        planterRepository.deleteAll();
        planterSettingsRepository.deleteAll();
        objectUnderTest = new SettingsService(planterRepository, planterSettingsRepository);
    }

    @Test
    void testSettingsService() {
        //Given
        //stworzyc instancje plantera i settings
        //zapisać je do planter repository i planter settings (DB)

        Planter planter = new Planter();
        planter.setId(null);
        planter.setName("planter_1");
        planter.setMacAddress("00:00:00:00:00:00");
        planter.setLastActivity(Instant.now().minusSeconds(6));

        PlanterSettings planterSettings = new PlanterSettings();
        planterSettings.setId(null);
        planterSettings.setPlanter(planter);
        planterSettings.setKey("sleep_time");
        planterSettings.setValue("666");
        planterSettings.setUpdateTimestamp(Instant.now());

        planterRepository.save(planter);
        planterSettingsRepository.save(planterSettings);

        CommandsRequesterWithTimestampDTO commandsRequesterWithTimestampDTO = new CommandsRequesterWithTimestampDTO();
        commandsRequesterWithTimestampDTO.setName(planter.getName());
        commandsRequesterWithTimestampDTO.setMacAddress(planter.getMacAddress());
        commandsRequesterWithTimestampDTO.setTimestamp(null);

        //When
        //Pobieram za pomocą object under test dane z endpointu (metoda z kontolera)

        SettingsDTO settingsDTO = objectUnderTest.getAwaitingSettingsUpdates(commandsRequesterWithTimestampDTO);
        System.out.println(settingsDTO.toString());

        //Then
        //Porównuję to co otrzymałem w When z instancjami, które stworzyłem w given
        //SettingsDTO porównane z tym co utworzyłem w teście

    }
}