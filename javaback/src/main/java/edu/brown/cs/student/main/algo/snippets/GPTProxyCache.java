package edu.brown.cs.student.main.algo.snippets;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import edu.brown.cs.student.main.server.config.APIKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class GPTProxyCache {
    /** Cache for mapping snippets to explanations */
    private final LoadingCache<String, String> cache;

    /**
     * Constructor that allows for the configuration of the cache based on developer's needs
     * @param size maximum size of the cache
     * @param time time for response to remain in the cache
     * @param unit unit of time
     * @param completionString StringBuilder representing the explanation string
     */
    public GPTProxyCache(int size, int time, TimeUnit unit, final StringBuilder completionString) {
        // build cache based on specified size, time, and unit of time
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(size)
            .expireAfterAccess(time, unit)
            .build(
                new CacheLoader<>() {
                    @NotNull
                    @Override
                    /**
                     * This overridden method loads a snippet into the cache
                     * and maps it to a String explanation
                     */
                    public String load(@NotNull String snippet) {
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

                        String explanation = completionString.toString();
                        // reset storage of explanation
                        completionString.setLength(0);

                        service.shutdownExecutor();
                        return explanation;
                    }

                    /**
                     * Helper method that acts as a lambda to build an explanation String
                     * by appending GPT messages
                     * @param chatCompletionChunk - chunks of GPT response
                     */
                    private void buildExplanation(ChatCompletionChunk chatCompletionChunk) {
                        String token = chatCompletionChunk.getChoices().get(0).getMessage().getContent();
                        if (token != null) {
                            completionString.append(chatCompletionChunk.getChoices().get(0).getMessage().getContent());
                        }
                    }
                });
    }

    /**
     * This method gets the explanation corresponding to the specified snippet
     * from the cache; if the snippet isn't already cached, it will be added
     * automatically.
     * @param snippet - String representing the code snippet
     * @return - String representing the explanation of the snippet
     */
    public String getExplanation(String snippet) {
        return this.cache.getUnchecked(snippet);
    }
}
