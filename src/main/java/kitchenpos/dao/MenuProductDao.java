package kitchenpos.dao;

import kitchenpos.menu.dto.MenuProductRequest;

import java.util.List;
import java.util.Optional;

public interface MenuProductDao {
    MenuProductRequest save(MenuProductRequest entity);

    Optional<MenuProductRequest> findById(Long id);

    List<MenuProductRequest> findAll();

    List<MenuProductRequest> findAllByMenuId(Long menuId);
}
