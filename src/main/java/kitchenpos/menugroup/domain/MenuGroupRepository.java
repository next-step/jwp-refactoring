package kitchenpos.menugroup.domain;

import java.util.List;

public interface MenuGroupRepository {
	MenuGroup save(MenuGroup menuGroup);

	List<MenuGroup> findAll();
}
