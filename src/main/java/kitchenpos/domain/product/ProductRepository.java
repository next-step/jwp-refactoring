package kitchenpos.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsAllByIdIn(List<Long> ids);

    List<Product> findAllByIdIn(List<Long> ids);
}
