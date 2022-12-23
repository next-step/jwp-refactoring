package kitchenpos.menu.port;

import kitchenpos.menu.domain.Menu;

import java.util.List;

public interface MenuPort {
    Menu save(Menu entity);

    List<Menu> findAll();

    List<Menu> findAllByMenuId(List<Long> menuIds);
}
