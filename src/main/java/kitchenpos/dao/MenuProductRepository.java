package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Optional;

public interface MenuProductRepository {
    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenu(Menu menu);
}
