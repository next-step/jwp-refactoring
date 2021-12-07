package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;

public class InMemoryMenuGroupDao extends InMemoryDao<MenuGroup> implements MenuGroupDao {

    @Override
    public boolean existsById(Long id) {
        return db.values().stream()
                .anyMatch(menuGroup -> menuGroup.getId() == id);
    }
}
