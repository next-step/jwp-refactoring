package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    boolean existsByProduct(Product product);
}
