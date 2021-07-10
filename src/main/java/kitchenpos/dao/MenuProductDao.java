package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {
    MenuProduct save(MenuProduct entity);

    Optional<MenuProduct> findBySeq(Long id);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
