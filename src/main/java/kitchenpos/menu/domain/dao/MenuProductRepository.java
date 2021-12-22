package kitchenpos.menu.domain.dao;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long>, MenuProductDao {

    @Override
    List<MenuProduct> findAllByMenuId(Long menuId);
}
