package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;

import java.util.List;

public class InMemoryMenuProductDao extends InMemoryDao<MenuProduct> implements MenuProductDao {

    public InMemoryMenuProductDao() {
        super("seq");
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return null;
    }
}
