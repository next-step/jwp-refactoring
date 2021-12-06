package kitchenpos.dao;

import kitchenpos.domain.Menu;

import java.util.List;

public class InMemoryMenuDao extends InMemoryDao<Menu> implements MenuDao {

    @Override
    public long countByIdIn(List<Long> ids) {
        return db.values().stream()
                .filter(menu -> ids.contains(menu.getId()))
                .count();
    }
}
