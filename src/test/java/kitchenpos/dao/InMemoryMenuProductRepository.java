package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class InMemoryMenuProductRepository extends InMemoryRepository<MenuProduct> implements MenuProductRepository {

    public InMemoryMenuProductRepository() {
        super("seq");
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return db.values().stream()
                .filter(menuProduct -> menuProduct.getMenuId() == menuId)
                .collect(Collectors.toList());
    }
}
