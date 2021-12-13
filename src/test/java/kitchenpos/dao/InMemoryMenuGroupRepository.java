package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;

public class InMemoryMenuGroupRepository extends InMemoryRepository<MenuGroup> implements MenuGroupRepository {

    @Override
    public boolean existsById(Long id) {
        return db.values().stream()
                .anyMatch(menuGroup -> menuGroup.getId() == id);
    }
}
