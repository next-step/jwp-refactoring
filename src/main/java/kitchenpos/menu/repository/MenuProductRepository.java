package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.*;

import kitchenpos.menu.domain.*;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
