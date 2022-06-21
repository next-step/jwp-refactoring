package kitchenpos.menu.dao;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.ServiceTestFactory.HONEY_RED_COMBO;

public class FakeMenuDao implements MenuDao {
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
        return Arrays.asList(HONEY_RED_COMBO);
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return 0;
    }
}
