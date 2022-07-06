package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    List<Menu> findByIdIn(List<Long> ids);
}
