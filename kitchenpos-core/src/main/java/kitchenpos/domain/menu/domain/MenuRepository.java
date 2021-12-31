package kitchenpos.domain.menu.domain;

import java.util.List;

public interface MenuRepository {
    Menu save(Menu menu);

    List<Menu> findAll();

    int countByIdIn(List<Long> menuIds);
}
