package kitchenpos.common.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Embeddable
public class PositiveNumber {

	private static final long MINIMUM = 0L;

	private Long number;

	private PositiveNumber(Long number) {
		this.number = number;
	}

	private static PositiveNumber of(Long number) {
		validate(number);
		return new PositiveNumber(number);
	}

	public static PositiveNumber valueOf(Long number) {
		return PositiveNumber.of(number);
	}

	public static void validate(Long number) {
		if (Objects.isNull(number)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "Null 일 수 없습니다");
		}
		if (number < MINIMUM) {
			throw new AppException(ErrorCode.WRONG_INPUT, "최소값({}) 이상이어야 합니다", MINIMUM);
		}
	}

	public Long toLong() {
		return number;
	}

}
