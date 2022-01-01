package kitchenpos.core.domain;

import java.util.List;

public interface MenuRepository {
    Menu save(Menu menu);

    List<Menu> findAll();

    int countByIdIn(List<Long> menuIds);
}
