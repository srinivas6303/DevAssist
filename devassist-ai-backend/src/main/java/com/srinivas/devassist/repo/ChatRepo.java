package com.srinivas.devassist.repo;

import com.srinivas.devassist.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepo extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findTop5ByUserIdOrderByTimestampDesc(Long userId);

    List<ChatMessage> findByUserIdOrderByTimestampAsc(Long id);
}