package kitchenpos.menugroup.domain;

import java.util.List;
import java.util.Optional;

public interface MenuGroupRepository {
	MenuGroup save(MenuGroup menuGroup);

	List<MenuGroup> findAll();

	Optional<MenuGroup> findById(Long id);
}
