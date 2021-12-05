package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryMenuProductDao extends InMemoryDao<MenuProduct> implements MenuProductDao {

    public InMemoryMenuProductDao() {
        super("seq");
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return db.values().stream()
                .filter(menuProduct -> menuProduct.getMenuId() == menuId)
                .collect(Collectors.toList());
    }
}
