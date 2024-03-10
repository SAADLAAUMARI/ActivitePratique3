package ma.enset.project;

import ma.enset.project.entities.Patient;
import ma.enset.project.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {
    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Patient p1=new Patient();
        p1.setNom("LAAUMARI");
        p1.setPrenom("Saad");
        p1.setDateNaissance(new Date());
        p1.setScore(123);
        p1.setMalade(true);

        Patient patient2=new Patient(null,"Yassine","Mohamed",new Date(),false,125);

        Patient patient3=Patient.builder()
                .nom("SAMIR")
                .prenom("Aya")
                .dateNaissance(new Date())
                .score(150)
                .malade(true)
                .build();
        patientRepository.save(p1);
        patientRepository.save(patient2);
        patientRepository.save(patient3);

    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}