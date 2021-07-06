package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

	@Column(nullable = false)
	private String value;

	protected Name() {}

	private Name(String value) {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException("이름은 빈문자열일 수 없습니다.");
		}
		this.value = value;
	}

	public static Name valueOf(String value) {
		return new Name(value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Name name1 = (Name)o;
		return Objects.equals(value, name1.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
