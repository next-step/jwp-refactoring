package kitchenpos.product.dao;

import kitchenpos.product.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
}
