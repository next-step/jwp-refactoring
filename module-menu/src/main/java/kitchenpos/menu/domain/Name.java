package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Name {

	private String name;

	protected Name() {
	}

	public Name(String name) {
		this.name = name;
	}

	public String value() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Name name = (Name)o;
		return this.name.equals(name.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
