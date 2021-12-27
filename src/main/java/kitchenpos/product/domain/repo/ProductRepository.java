package kitchenpos.product.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
