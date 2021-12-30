package kitchenpos.common.domain;

import javax.persistence.Embeddable;

import org.springframework.util.StringUtils;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Embeddable
public class Name {

	private String name;

	protected Name() {
	}

	private Name(String name) {
		this.name = name;
	}

	public static Name of(String name) {
		validate(name);
		return new Name(name);
	}

	public String toText() {
		return name;
	}

	private static void validate(String name) {
		if (!StringUtils.hasText(name)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "이름은 비어있으면 안됩니다");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Name name1 = (Name)o;

		return name.equals(name1.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
