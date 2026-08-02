package org.l4ncet.telegrambot.service;

import org.l4ncet.telegrambot.bot.session.ProposalCreationSession;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ProposalSessionService {

    private final ConcurrentMap<Long, ProposalCreationSession> sessions = new ConcurrentHashMap<>();

    public void start (Long telegramId, Long orderId){
        ProposalCreationSession session = new ProposalCreationSession(orderId);

        sessions.put(telegramId, session);
    }

    public Optional<ProposalCreationSession> find(Long telegramId){
        return Optional.ofNullable(sessions.get(telegramId));
    }

    public boolean exists(Long telegramId){
        return sessions.containsKey(telegramId);
    }

    public void clear(Long telegramId){
        sessions.remove(telegramId);
    }
}
