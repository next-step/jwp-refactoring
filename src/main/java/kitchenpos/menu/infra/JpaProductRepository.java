package kitchenpos.menu.infra;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long>, ProductRepository {
}
