package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
