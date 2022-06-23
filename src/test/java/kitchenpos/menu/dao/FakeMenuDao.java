package kitchenpos.menu.dao;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.infrastructure.MenuDao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.ServiceTestFactory.HONEY_RED_COMBO;

public class FakeMenuDao implements MenuDao {
    private final List<Menu> menus = Arrays.asList(HONEY_RED_COMBO);

    @Override
    public Menu save(Menu entity) {
        return entity;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        return menus;
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menus.stream()
                .filter(menu -> ids.contains(menu.getId()))
                .count();
    }
}
