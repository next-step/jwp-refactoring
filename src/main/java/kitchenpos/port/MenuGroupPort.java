package kitchenpos.port;

import kitchenpos.domain.MenuGroup;

import java.util.List;
import java.util.Optional;

public interface MenuGroupPort {
    MenuGroup save(MenuGroup entity);

    MenuGroup findById(Long id);

    List<MenuGroup> findAll();
}
