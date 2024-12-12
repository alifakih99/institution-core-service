package com.itg.institution.shared.init;

import com.itg.institution.entities.UserEntity;
import com.itg.institution.repository.UserRepository;
import com.itg.institution.shared.security.CustomPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner
{
    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args)
    {
        log.info("Adding Default Users");
        UserEntity user1 = new UserEntity().setFirstName("Ali")
                .setLastName("Fakih")
                .setUsername("ali299")
                .setPassword(customPasswordEncoder.encode("test"))
                .setRole("SUPER_ADMIN");
        userRepository.save(user1);

        UserEntity user2 = new UserEntity().setFirstName("ITG")
                .setLastName("Holding")
                .setUsername("itg0")
                .setPassword(customPasswordEncoder.encode("test"))
                .setRole("SUPER_ADMIN");
        userRepository.save(user2);

        UserEntity user3 = new UserEntity().setFirstName("ITG")
                .setLastName("Holding")
                .setUsername("itg1")
                .setPassword(customPasswordEncoder.encode("test"))
                .setRole("ADMIN");
        userRepository.save(user3);

        UserEntity user4 = new UserEntity().setFirstName("ITG")
                .setLastName("Holding")
                .setUsername("itg2")
                .setPassword(customPasswordEncoder.encode("test"))
                .setRole("USER");
        userRepository.save(user4);
        log.info("Finishing Adding Default Users");
    }
}
