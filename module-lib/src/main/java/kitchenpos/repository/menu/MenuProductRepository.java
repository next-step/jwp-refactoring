package kitchenpos.repository.menu;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
