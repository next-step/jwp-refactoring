package kitchenpos.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = " "
            + " SELECT COUNT(p.id) "
            + "   FROM Product  p "
            + "  WHERE p.id IN (:ids)")
    Integer countAllByIds(@Param("ids") List<Long> ids);
}
