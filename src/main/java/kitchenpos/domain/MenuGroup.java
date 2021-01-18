package kitchenpos.domain;

import kitchenpos.common.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "menu_group")
public class MenuGroup extends BaseIdEntity {

	@Column(name = "name", nullable = false)
	private String name;

	protected MenuGroup() {
	}

	public MenuGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
