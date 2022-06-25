package kitchenpos.product.repository;

import kitchenpos.product.domain.ProductV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductV2, Long> {
}
