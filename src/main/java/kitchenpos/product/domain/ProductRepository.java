package kitchenpos.product.domain;

import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findByIdIn(List<Long> productIds);
}
