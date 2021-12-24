package kitchenpos.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name extends Value<Name> {
	@Column(name = "name", nullable = false)
	private String value;

	protected Name() {
	}

	public static Name from(String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("이름은 빈 값일 수 없습니다.");
		}

		Name name = new Name();
		name.value = value;
		return name;
	}

	public String getValue() {
		return value;
	}
}
