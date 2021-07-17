package kitchenpos.dao;

import kitchenpos.domain.MenuRequest;

import java.util.List;
import java.util.Optional;

public interface MenuDao {
    MenuRequest save(MenuRequest entity);

    Optional<MenuRequest> findById(Long id);

    List<MenuRequest> findAll();

    long countByIdIn(List<Long> ids);
}
