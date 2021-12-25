package kitchenpos.menugroup.infra.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;

public class InMemoryMenuGroupRepository implements MenuGroupRepository {
	private final Map<Long, MenuGroup> menuGroups = new HashMap<>();

	@Override
	public MenuGroup save(MenuGroup menuGroup) {
		menuGroups.put(menuGroup.getId(), menuGroup);
		return menuGroup;
	}

	@Override
	public List<MenuGroup> findAll() {
		return new ArrayList<>(menuGroups.values());
	}

	@Override
	public Optional<MenuGroup> findById(Long id) {
		return Optional.ofNullable(menuGroups.get(id));
	}
}
