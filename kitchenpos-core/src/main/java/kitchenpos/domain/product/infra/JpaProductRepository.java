package kitchenpos.domain.product.infra;

import kitchenpos.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.id in (:ids)")
    List<Product> findAllByIds(@Param("ids") List<Long> productIds);

}
