package kitchenpos.product.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, Long> {
}
