package kitchenpos.product.domain;

import kitchenpos.common.exception.NotFoundProductException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    default Product findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundProductException::new);
    }
}
