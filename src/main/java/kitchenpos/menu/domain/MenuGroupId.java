package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupId {

	@Column(name = "menu_group_id", nullable = false)
	private Long id;

	protected MenuGroupId() {}

	public MenuGroupId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
