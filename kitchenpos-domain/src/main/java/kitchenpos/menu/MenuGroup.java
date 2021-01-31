package kitchenpos.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;

	protected MenuGroup() {
	}

	private MenuGroup(String name) {
		this.name = name;
	}

	public static MenuGroupBuilder builder() {
		return new MenuGroupBuilder();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static final class MenuGroupBuilder {
		private String name;

		private MenuGroupBuilder() {
		}

		public MenuGroupBuilder name(String name) {
			this.name = name;
			return this;
		}

		public MenuGroup build() {
			return new MenuGroup(name);
		}
	}
}
