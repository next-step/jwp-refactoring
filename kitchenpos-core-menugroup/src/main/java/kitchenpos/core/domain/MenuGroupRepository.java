package kitchenpos.core.domain;

import java.util.List;

public interface MenuGroupRepository {
    MenuGroup save(MenuGroup menuGroup);

    List<MenuGroup> findAll();

    boolean existsById(long id);
}
