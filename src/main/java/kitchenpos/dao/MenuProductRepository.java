package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findAllByMenuId(Long menuId);
}
