package kitchenpos.menu.dao;

import static kitchenpos.ServiceTestFactory.FAVORITE_MENU_GROUP;
import static kitchenpos.ServiceTestFactory.NEW_MENU_GROUP;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.infrastructure.MenuGroupDao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FakeMenuGroupDao implements MenuGroupDao {
    @Override
    public MenuGroup save(MenuGroup entity) {
        return entity;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return Arrays.asList(NEW_MENU_GROUP, FAVORITE_MENU_GROUP).stream()
                .filter(menuGroup -> menuGroup.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<MenuGroup> findAll() {
        return Arrays.asList(NEW_MENU_GROUP, FAVORITE_MENU_GROUP);
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}
