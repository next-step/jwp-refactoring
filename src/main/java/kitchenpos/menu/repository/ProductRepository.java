package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.*;

import kitchenpos.menu.domain.*;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
