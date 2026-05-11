package com.srinivas.devassist.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.srinivas.devassist.dto.ChatResponse;
import com.srinivas.devassist.model.*;
import com.srinivas.devassist.repo.ChatRepo;
import com.srinivas.devassist.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepo chatRepo;
    @Autowired
    private UserRepo userRepo;

    private final Client client = new Client();

    public String getResponse(String username, String userMessage) {
        try {
            User user = userRepo.findByUsername(username);

            if (user == null) {
                return "User not found";
            }

            List<ChatMessage> history =
                    chatRepo.findTop5ByUserIdOrderByTimestampDesc(user.getId());

            Collections.reverse(history);

            StringBuilder prompt = new StringBuilder("Answer the user's question clearly and directly. "
                    + "Do not assume typos. "
                    + "Do not restrict to Java. "
                    + "If the question is about any language or topic, answer correctly.\n\n"
                    + "Question: ");

            for (ChatMessage msg : history) {
                if (msg.getRole() == Role.USER) {
                    prompt.append("User: ").append(msg.getMessage()).append("\n");
                } else {
                    prompt.append("AI: ").append(msg.getMessage()).append("\n");
                }
            }

            prompt.append("User: ").append(userMessage);

            GenerateContentConfig config=GenerateContentConfig
                    .builder()
                    .temperature(0.9F)
                    .build();

            GenerateContentResponse response =
                    client.models.generateContent("gemini-3-flash-preview", prompt.toString(), config);

            String aiResponse = response.text() != null ? response.text() : "No response";

            chatRepo.save(new ChatMessage(user, Role.USER, userMessage, LocalDateTime.now()));
            chatRepo.save(new ChatMessage(user, Role.AI, aiResponse, LocalDateTime.now()));

            return aiResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public List<ChatResponse> getUserHistory(String username) {

        User user = userRepo.findByUsername(username);

        if (user == null) {
            return Collections.emptyList();
        }

        List<ChatMessage> chatResponses=chatRepo.findByUserIdOrderByTimestampAsc(user.getId());

        return chatResponses.stream()
                .map(msg -> new ChatResponse(msg.getRole().name(),msg.getMessage()))
                .toList();
    }

}