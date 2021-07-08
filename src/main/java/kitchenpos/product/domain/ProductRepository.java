package kitchenpos.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
