package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = " "
            + " SELECT COUNT(p.id) "
            + "   FROM Product  p "
            + "  WHERE p.id IN (:ids)")
    Integer countAllByIds(List<Long> ids);
}
