package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeMenuProductDao implements MenuProductDao {
    private Map<Long, MenuProduct> map = new HashMap<>();
    private Long key = 1L;

    @Override
    public MenuProduct save(MenuProduct menuProduct) {
        menuProduct.createId(key);
        map.put(key, menuProduct);
        key++;
        return menuProduct;
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return null;
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return map.values().stream()
                .filter(menuProduct -> menuId.equals(menuProduct.getMenuId()))
                .collect(Collectors.toList());
    }
}
