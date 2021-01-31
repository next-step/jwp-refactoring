package kitchenpos.common;

import java.util.List;

public class ValidationUtils {
	public static void validateListSize(List<?> list1, List<?> list2, String message) {
		if (list1.size() != list2.size()) {
			throw new IllegalArgumentException(message);
		}
	}
}
