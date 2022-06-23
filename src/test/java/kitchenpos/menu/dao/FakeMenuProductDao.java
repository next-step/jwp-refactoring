package kitchenpos.menu.dao;

import static kitchenpos.ServiceTestFactory.HONEY_MENU_PRODUCT;
import static kitchenpos.ServiceTestFactory.RED_MENU_PRODUCT;

import java.util.Arrays;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.infrastructure.MenuProductDao;

import java.util.List;
import java.util.Optional;

public class FakeMenuProductDao implements MenuProductDao {
    @Override
    public MenuProduct save(MenuProduct entity) {
        return entity;
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
        return Arrays.asList(HONEY_MENU_PRODUCT, RED_MENU_PRODUCT);
    }
}
