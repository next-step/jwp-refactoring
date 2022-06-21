package kitchenpos.menu.dao;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Optional;

public class FakeMenuProductDao implements MenuProductDao {
    @Override
    public MenuProduct save(MenuProduct entity) {
        return null;
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<MenuProduct> findAll() {
        return null;
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return null;
    }
}
