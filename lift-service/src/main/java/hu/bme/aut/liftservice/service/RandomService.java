package hu.bme.aut.liftservice.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomService {

    private final Random random;

    public RandomService(Random random) {
        this.random = random;
    }

    public int getNextInt(int bound) {
        return random.nextInt(bound);
    }
}
