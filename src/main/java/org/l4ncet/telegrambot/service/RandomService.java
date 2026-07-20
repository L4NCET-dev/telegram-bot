package org.l4ncet.telegrambot.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class RandomService {

    public int generate(){
        return ThreadLocalRandom.current().nextInt(1,3);
    }
}
