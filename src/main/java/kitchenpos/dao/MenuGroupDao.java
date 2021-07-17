package kitchenpos.dao;

import kitchenpos.domain.MenuGroupRequest;

import java.util.List;
import java.util.Optional;

public interface MenuGroupDao {
    MenuGroupRequest save(MenuGroupRequest entity);

    Optional<MenuGroupRequest> findById(Long id);

    List<MenuGroupRequest> findAll();

    boolean existsById(Long id);
}
