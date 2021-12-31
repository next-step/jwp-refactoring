package kitchenpos.product.domain;

import kitchenpos.exception.NotFoundProductException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    default Product findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundProductException::new);
    }
}
