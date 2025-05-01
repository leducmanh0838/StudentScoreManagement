package com.ldm.components;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {

    @Autowired
    private Map<String, MessageSource> customMessageSources;

//    private static final List<String> NEEDED_KEYS = List.of(
//        "USER_LIST", "USER_NAME" // có thể truyền thêm từ controller nếu muốn tùy biến
//    );
//
//    public Map<String, String> getMessages(String lang) {
//        MessageSource ms = customMessageSources.getOrDefault(lang, customMessageSources.get("vi"));
//        Map<String, String> messages = new HashMap<>();
//
//        for (String key : NEEDED_KEYS) {
//            String value = ms.getMessage(key, null, Locale.ROOT);
//            messages.put(key, value);
//        }
//        return messages;
//    }


    public Map<String, String> getMessages(String lang, List<String> keys) {
        MessageSource ms = customMessageSources.getOrDefault(lang, customMessageSources.get("vi"));
        Map<String, String> messages = new HashMap<>();

        for (String key : keys) {
            String value = ms.getMessage(key, null, Locale.ROOT);
            messages.put(key, value);
        }
        return messages;
    }
}
