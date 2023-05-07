package edu.brown.cs.student.main.algo.snippets;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import edu.brown.cs.student.main.server.config.APIKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GPTRequester {
    private final StringBuilder completionString;

    public GPTRequester() {
        this.completionString = new StringBuilder();
    }

    /**
     * Helper method that acts as a lambda to build an explanation String
     * by appending GPT messages
     * @param chatCompletionChunk - chunks of GPT response
     */
    private void buildExplanation(ChatCompletionChunk chatCompletionChunk) {
        String token = chatCompletionChunk.getChoices().get(0).getMessage().getContent();
        if (token != null) {
            this.completionString.append(chatCompletionChunk.getChoices().get(0).getMessage().getContent());
        }
    }

    /**
     * This method calls the OpenAI API to get an explanation for a snippet given to it as a String.
     * @param snippet - snippet to get explanation for.
     * @return - an explanation for the snippet, generated through ChatGPT.
     */
    public String getExplanation(String snippet) {
        // credit: https://github.com/TheoKanning/openai-java/blob/main/example/src/main/java/example/OpenAiApiExample.java
        String token = APIKeys.API_KEY;
        OpenAiService service = new OpenAiService(token);

        // store messages sent to api
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "Explain this code snippet: \n" + snippet);
        messages.add(systemMessage);
        // request gpt 3.5
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .temperature(1.0)
                .n(1)
                .maxTokens(500)
                .logitBias(new HashMap<>())
                .build();
        // build an explanation based on messages received from GPT
        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(this::buildExplanation);

        String explanation = this.completionString.toString();
        // reset storage of explanation
        this.completionString.setLength(0);

        service.shutdownExecutor();
        return explanation;
    }

}
