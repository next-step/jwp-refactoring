package kitchenpos.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuGroupRequest;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
