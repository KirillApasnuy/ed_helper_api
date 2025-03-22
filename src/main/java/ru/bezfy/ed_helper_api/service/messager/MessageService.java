package ru.bezfy.ed_helper_api.service.messager;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bezfy.ed_helper_api.api.model.AssistantResponse;
import ru.bezfy.ed_helper_api.api.model.ClientMessageBody;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.UserChat;
import ru.bezfy.ed_helper_api.model.UserMessage;
import ru.bezfy.ed_helper_api.model.dao.GPTModelDAO;
import ru.bezfy.ed_helper_api.model.dao.LocalUserDAO;
import ru.bezfy.ed_helper_api.model.dao.UserMessageDAO;
import ru.bezfy.ed_helper_api.model.deepseek.DeepSeekGPTResponse;
import ru.bezfy.ed_helper_api.model.openai.ChatGPTResponse;
import ru.bezfy.ed_helper_api.repository.DeepGramRepository;
import ru.bezfy.ed_helper_api.repository.DeepSeekRepository;
import ru.bezfy.ed_helper_api.repository.OpenAiRepository;
import ru.bezfy.ed_helper_api.service.AssistantService;
import ru.bezfy.ed_helper_api.service.ChatService;
import ru.bezfy.ed_helper_api.service.QuestionAnswerService;
import ru.bezfy.ed_helper_api.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final GPTModelDAO gptModelDAO;
    private final LocalUserDAO localUserDAO;
    private final OpenAiRepository openAiRepositoryRepository;
    private final DeepSeekRepository deepSeekRepositoryRepository;
    private final DeepGramRepository deepGramRepositoryRepository;
    private final QuestionAnswerService questionAnswerService;
    private final ChatService chatService;
    private final ResponseService responseService;
    private final UserService userService;
    private final UserMessageDAO userMessageDAO;

    public MessageService(GPTModelDAO gptModelDAO, LocalUserDAO localUserDAO, OpenAiRepository openAiRepositoryRepository, DeepSeekRepository deepSeekRepositoryRepository, DeepGramRepository deepGramRepositoryRepository, QuestionAnswerService questionAnswerService, ChatService chatService, UserMessageDAO userMessageDAO, ResponseService responseService, UserService userService) {
        this.gptModelDAO = gptModelDAO;
        this.localUserDAO = localUserDAO;
        this.openAiRepositoryRepository = openAiRepositoryRepository;
        this.deepSeekRepositoryRepository = deepSeekRepositoryRepository;
        this.deepGramRepositoryRepository = deepGramRepositoryRepository;
        this.questionAnswerService = questionAnswerService;
        this.chatService = chatService;
        this.userMessageDAO = userMessageDAO;
        this.responseService = responseService;
        this.userService = userService;
    }

    public ResponseEntity<?> sendOpenAiTextMessage(LocalUser user, ClientMessageBody message) {
        UserChat chat;

        // Если chatId не указан или пустой, создаем новый чат
        if (message.getChatId() == null || message.getChatId() == 0) {
            chat = chatService.createChat(user, Arrays.stream(message.getText().split(" ")).limit(3).collect(Collectors.joining(" ")));
        } else {
            // Если chatId указан, пробуем найти существующий чат
            UserChat optionalChat = chatService.getChat(message.getChatId());

            if (optionalChat == null) {
                chat = chatService.createChat(user, Arrays.stream(message.getText().split(" ")).limit(3).collect(Collectors.joining(" ")));
//                return ResponseEntity.badRequest().body("Чат с указанным ID не найден.");
            } else {
                chat = optionalChat;
            }
        }

        UserMessage userMessage = new UserMessage();
        userMessage.setText(message.getText());
        userMessage.setUser(true);
        userMessage.setAudioUrl(message.getAudioUrl());
        userMessage.setImageUrl(message.getImageUrl());
        userMessage.setUserChat(chat);
        if (!chatService.addMessage(chat.getId(), userMessageDAO.save(userMessage))) {
            return ResponseEntity.badRequest().body("Failed to add message");
        }
        if (!userService.accessSendMessage(user)) {
            System.out.println(userService.accessSendMessage(user));
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You have exceeded your monthly message limit or your subscription has expired");
        }
        // Увеличиваем счётчик генераций пользователя
        user.setCountGenerationInLastMonth(user.getCountGenerationInLastMonth() + 1);
        localUserDAO.save(user);

        // Проверяем наличие предопределённого ответа
        Optional<AssistantResponse> predefinedResponse = questionAnswerService.findBestMatch(message, 0.5);
        System.out.println(1);
        if (predefinedResponse.isPresent()) {
            AssistantResponse assistantResponse = predefinedResponse.get();
            assistantResponse.setChatId(chat.getId());

            UserMessage responseMessage = new UserMessage();
            responseMessage.setUserChat(chat);
            responseMessage.setText(assistantResponse.getMessage());
            responseMessage.setMedia(assistantResponse.getMedia());
            chatService.addMessage(chat.getId(), userMessageDAO.save(responseMessage));
            return ResponseEntity.ok(assistantResponse);
        }

//        return ResponseEntity.badRequest().build();

        // Отправляем запрос в OpenAI
        ResponseEntity<String> response = openAiRepositoryRepository.sendMessage(message);
        System.out.println(response.getBody());

        try {
            ChatGPTResponse chatGPTResponse = responseService.parseOpenAiResponse(response.getBody());
            String aiMessage = chatGPTResponse.choices.get(0).message.content;
            UserMessage responseMessage = new UserMessage();
            responseMessage.setText(aiMessage);
            // Добавляем сообщения в чат
            responseMessage.setUserChat(chat);
            chatService.addMessage(chat.getId(), userMessageDAO.save(responseMessage));

            // Обновляем информацию о токенах
            user.setUsedTokensInPeriodInLastMonth(
                    user.getUsedTokensInPeriodInLastMonth() + chatGPTResponse.usage.totalTokens
            );
            localUserDAO.save(user);

            // Формируем ответ
            AssistantResponse assistantResponse = new AssistantResponse();
            assistantResponse.setMessage(aiMessage);
            assistantResponse.setChatId(chat.getId());
            return ResponseEntity.ok(assistantResponse);
        } catch (JsonProcessingException e) {
            UserMessage errorMessage = new UserMessage();
            errorMessage.setText("An error occurred while sending a message");
            chatService.addMessage(chat.getId(), userMessageDAO.save(errorMessage));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки JSON");
        }
    }


    public ResponseEntity<String> sendDeepSeekTextMessage(LocalUser user, ClientMessageBody message) {
        ResponseEntity<String> response = deepSeekRepositoryRepository.sendMessage(message);
        System.out.println(response.getBody());
        try {
            DeepSeekGPTResponse deepSeekGPTResponse = responseService.parseDeepSeekResponse(response.getBody());
            System.out.println(deepSeekGPTResponse.id);
            user.setUsedTokensInPeriodInLastMonth((long) deepSeekGPTResponse.usage.total_tokens);
            System.out.println(user.getUsedTokensInPeriodInLastMonth());
            localUserDAO.save(user);
            return response;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("JsonProcessingException");
        }
    }

    public ResponseEntity<?> sendVoiceMessage(LocalUser user, ClientMessageBody message, MultipartFile file) throws IOException {
        String transcript = deepGramRepositoryRepository.transcribeAudio(file);
        if (transcript != null) {
            message.setText(transcript);
            return sendOpenAiTextMessage(user, message);
        }
        throw new IOException();
    }
}
