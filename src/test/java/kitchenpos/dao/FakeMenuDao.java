package kitchenpos.dao;

import kitchenpos.domain.Menu;

import java.util.*;
import java.util.stream.Collectors;

public class FakeMenuDao implements MenuDao {
    private Map<Long, Menu> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public Menu save(Menu menu) {
        menu.createId(key);
        map.put(key, menu);
        key++;
        return menu;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return 0;
    }
}
