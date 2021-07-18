package com.paypal.bfs.test.bookingserv.i18n;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Provides utility methods for internationalization
 *
 */
@Component
public class MessageUtil {
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Returns the message as per user's current locale.
	 * Current locale value would be fetched using Accept-Language
	 * header value
	 * @param key - Message key defined in constants and message 
	 *              properties file
	 * @return Localized message
	 */
	public String getLocalizedMessage(String key) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, null, key, locale);
	}
	
	/**
	 * Returns the message as per user's current locale.
	 * Current locale value would be fetched using Accept-Language
	 * header value
	 * @param key - Message key defined in constants and message 
	 *              properties file
	 * @return Localized message
	 */
	public String getLocalizedMessageWithParameters(String key, String[] parameters) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, parameters, key, locale);
	}


}
