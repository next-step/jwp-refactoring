package kitchenpos.product.infrastructure.repository;

import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(List<Long> id);
}
