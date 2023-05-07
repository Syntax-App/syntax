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
     * @param gptRequester - an instance of GPTRequester, which contains code to interact with the OpenAI API.
     * @param size maximum size of the cache
     * @param time time for response to remain in the cache
     * @param unit unit of time
     */
    public GPTProxyCache(GPTRequester gptRequester, int size, int time, TimeUnit unit) {
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
                     * and maps it to a String explanation using a requestor
                     */
                    public String load(@NotNull String snippet) {
                        return gptRequester.getExplanation(snippet);
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
