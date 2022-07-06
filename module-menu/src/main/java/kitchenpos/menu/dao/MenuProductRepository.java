package kitchenpos.menu.dao;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenuId(Long menuId);
}
