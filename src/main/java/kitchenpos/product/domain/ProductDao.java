package kitchenpos.product.domain;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
}
