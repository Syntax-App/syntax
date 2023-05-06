package edu.brown.cs.student.main.algo.snippets;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import edu.brown.cs.student.main.algo.snippets.Snippets.SnippetsJSON;
import edu.brown.cs.student.main.server.config.APIKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class GPTProxyCache {
    /**Cache for storing WeatherResponse instances*/
    private final LoadingCache<String, String> cache;

    /**
     * Constructor that allows for the configuration of the cache based on developer's needs
     * @param size maximum size of the cache
     * @param time time for response to remain in the cache
     * @param unit unit of time
     */
    public GPTProxyCache(int size, int time, TimeUnit unit, SnippetsJSON json, final StringBuilder completionString) {
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(size)
            .expireAfterAccess(time, unit)
            .build(
                new CacheLoader<>() {
                    @NotNull
                    @Override
                    public String load(@NotNull String snippet) {
                        // credit: https://github.com/TheoKanning/openai-java/blob/main/example/src/main/java/example/OpenAiApiExample.java
                        String token = APIKeys.API_KEY;
                        OpenAiService service = new OpenAiService(token);

                        final List<ChatMessage> messages = new ArrayList<>();
                        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "Explain this code snippet: \n" + snippet);
                        messages.add(systemMessage);
                        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                            .builder()
                            .model("gpt-3.5-turbo")
                            .messages(messages)
                            .temperature(0.5)
                            .n(1)
                            .maxTokens(500)
                            .logitBias(new HashMap<>())
                            .build();

                        service.streamChatCompletion(chatCompletionRequest)
                            .doOnError(Throwable::printStackTrace)
                            .blockingForEach(this::buildExplanation);

                        String explanation = completionString.toString();
                        // reset explanation to respond with
                        completionString.setLength(0);

                        service.shutdownExecutor();
                        return explanation;
                    }

                    private void buildExplanation(ChatCompletionChunk chatCompletionChunk) {
                        String token = chatCompletionChunk.getChoices().get(0).getMessage().getContent();
                        if (token != null) {
                            completionString.append(chatCompletionChunk.getChoices().get(0).getMessage().getContent());
                        }
                    }
                });
    }

    public String getExplanation(String snippet) {
        return this.cache.getUnchecked(snippet);
    }
}
