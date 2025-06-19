package de.lorenz.ticketsystem.service.lang;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LanguageService {

    MessageSource messageSource;

    public String getMessage(String key, String langCode) {
        String lang = (langCode == null || langCode.isBlank()) ? "en" : langCode;
        Locale locale = Locale.forLanguageTag(lang);
        return messageSource.getMessage(key, null, locale);
    }

}
