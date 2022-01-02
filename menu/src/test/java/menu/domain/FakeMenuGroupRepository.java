package menu.domain;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;

import java.util.*;


public class FakeMenuGroupRepository implements MenuGroupRepository {
    private Map<Long, MenuGroup> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public MenuGroup save(MenuGroup inputMenuGroup) {
        MenuGroup menuGroup = MenuGroup.of(key, inputMenuGroup.getName());
        map.put(key, menuGroup);
        key++;
        return menuGroup;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean existsById(Long id) {
        return map.containsKey(id);
    }
}
