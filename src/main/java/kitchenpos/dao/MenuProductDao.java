package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenuId(Long menuId);
}
