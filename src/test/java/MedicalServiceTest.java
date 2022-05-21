import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceTest {
    @Test
    void MedicalServiceImplTestBlood() {
        PatientInfoFileRepository repository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        String id = repository.add(
                new PatientInfo("Иван", "Петров",
                        LocalDate.of(1986, 6, 21),
                        new HealthInfo(new BigDecimal(36.7), new BloodPressure(120, 85))));
        Mockito.when(repository.getById(id))
                .thenReturn(new PatientInfo("Иван", "Петров",
                        LocalDate.of(1986, 6, 21),
                        new HealthInfo(new BigDecimal(36.7), new BloodPressure(120, 85))));
        Mockito.doThrow(new RuntimeException( )).when(alertService).send(Mockito.anyString( ));
        MedicalServiceImpl medService = new MedicalServiceImpl(repository, alertService);
        medService.checkBloodPressure(id, new BloodPressure(120, 85));
    }

    @Test
    void MedicalServiceImplTestTemperature() {
        PatientInfoFileRepository repository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        String id1 = repository.add(
                new PatientInfo("Семен", "Слепаков", LocalDate.of(1979, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 80)))
        );
        Mockito.when(repository.getById(id1))
                .thenReturn(new PatientInfo("Семен", "Слепаков", LocalDate.of(1979, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 80))));
        Mockito.doThrow(new RuntimeException( )).when(alertService).send(Mockito.anyString( ));

        MedicalServiceImpl medService = new MedicalServiceImpl(repository, alertService);
        medService.checkTemperature(id1, new BigDecimal(36.6));
    }

    @Test
    void MedicalServiceImplTest() {
        PatientInfoFileRepository repository = Mockito.mock(PatientInfoFileRepository.class);
        SendAlertService alertService = Mockito.mock(SendAlertService.class);

        String id2 = repository.add(
                new PatientInfo("Петр", "Иванов", LocalDate.of(1956, 11, 3),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(130, 90)))
        );
        Mockito.when(repository.getById(id2))
                .thenReturn(new PatientInfo("Петр", "Иванов", LocalDate.of(1956, 11, 3),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(130, 90))));
        Mockito.doThrow(new RuntimeException( )).when(alertService).send(Mockito.anyString( ));
//        ArgumentCaptor<String> sendAlert = ArgumentCaptor.forClass(String.class);
//        Mockito.verify(alertService).send(sendAlert.capture());
//        Assertions.assertNull(sendAlert.getValue());
        MedicalServiceImpl medService = new MedicalServiceImpl(repository, alertService);
        medService.checkBloodPressure(id2, new BloodPressure(130, 90));
        medService.checkTemperature(id2, new BigDecimal(36.6));
        Mockito.verify(alertService, Mockito.times(0)).send(id2);

    }


}
