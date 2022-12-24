package kitchenpos.menu.port;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.Optional;

public interface MenuProductPort {
    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findById(Long id);

    List<MenuProduct> findAll();
}
