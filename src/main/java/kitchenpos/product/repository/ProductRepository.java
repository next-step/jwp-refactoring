package kitchenpos.product.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
