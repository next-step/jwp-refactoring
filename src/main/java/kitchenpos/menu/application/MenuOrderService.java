package kitchenpos.menu.application;

import java.util.Optional;

import kitchenpos.menu.domain.Menu;

public interface MenuOrderService {
    Optional<Menu> findMenuById(Long menuId);
}
