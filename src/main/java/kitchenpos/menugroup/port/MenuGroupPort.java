package kitchenpos.menugroup.port;

import kitchenpos.menugroup.domain.MenuGroup;

import java.util.List;

public interface MenuGroupPort {
    MenuGroup save(MenuGroup entity);

    MenuGroup findById(Long id);

    List<MenuGroup> findAll();
}
