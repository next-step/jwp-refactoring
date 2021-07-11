package kitchenpos.manugroup.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	protected MenuGroup() {

	}

	public MenuGroup(String name) {
		this.name = name;
	}

	public MenuGroup(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
