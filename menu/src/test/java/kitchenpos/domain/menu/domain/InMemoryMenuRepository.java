package kitchenpos.domain.menu.domain;

import kitchenpos.common.InMemoryRepository;

import java.util.List;

public class InMemoryMenuRepository extends InMemoryRepository<Menu> implements MenuRepository {

    @Override
    public long countByIdIn(List<Long> ids) {
        return db.values().stream()
                .filter(menu -> ids.contains(menu.getId()))
                .count();
    }
}
