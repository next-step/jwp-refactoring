package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenu(Menu menu);
    MenuProduct findByMenuAndProduct(Menu menu, Product product);
}
