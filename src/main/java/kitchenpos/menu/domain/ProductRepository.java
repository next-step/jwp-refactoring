package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByIdIn(List<Long> productIds);
}
