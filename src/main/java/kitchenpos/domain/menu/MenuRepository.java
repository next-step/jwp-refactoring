package kitchenpos.domain.menu;

import java.util.List;

public interface MenuRepository {
    Menu save(Menu menu);
    List<Menu> findAll();
}
