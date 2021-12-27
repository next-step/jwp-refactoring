package kitchenpos.menu.infra.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

public class InMemoryMenuRepository implements MenuRepository {
	private final Map<Long, Menu> menus = new HashMap<>();

	@Override
	public List<Menu> findAll() {
		return new ArrayList<>(menus.values());
	}

	@Override
	public Menu save(Menu menu) {
		menus.put(menu.getId(), menu);
		return menu;
	}

	@Override
	public Optional<Menu> findById(Long id) {
		return Optional.ofNullable(menus.get(id));
	}

	@Override
	public List<Menu> findAllByIdIn(List<Long> ids) {
		return menus.values()
			.stream()
			.filter(menu -> ids.contains(menu.getId()))
			.collect(Collectors.toList());
	}
}
