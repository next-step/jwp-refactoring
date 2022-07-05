package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    // TODO 삭제 필요
    List<MenuProduct> findAllByMenuId(Long menuId);
}
