package kitchenpos.menu.domain;

import java.util.List;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenu(Menu menu);
    MenuProduct findByMenuAndProduct(Menu menu, Product product);
}
