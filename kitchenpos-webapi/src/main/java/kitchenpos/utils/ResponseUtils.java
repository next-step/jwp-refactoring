package kitchenpos.utils;

import java.net.URI;

public class ResponseUtils {

	private static final String URL_DELIMITER = "/";

	private ResponseUtils() {
	}

	public static URI createdUrl(String baseUrl, Long id) {
		return URI.create(baseUrl + URL_DELIMITER + id);
	}
}
