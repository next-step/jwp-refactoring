package kitchenpos.product.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Repository(value = "ToBeJpaProductRepository")
public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, Long> {
}
