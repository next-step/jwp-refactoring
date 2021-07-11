package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.menu.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
