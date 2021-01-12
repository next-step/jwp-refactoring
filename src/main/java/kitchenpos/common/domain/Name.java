package kitchenpos.common.domain;

import javax.persistence.Embeddable;

import io.micrometer.core.instrument.util.StringUtils;

@Embeddable
public class Name {
	private String name;

	protected Name() {
	}

	public Name(String name) {
		validate(name);
		this.name = name;
	}

	private void validate(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("이름은 NULL 이나 공백 일 수 없습니다.");
		}
	}

	public String value() {
		return this.name;
	}
}
