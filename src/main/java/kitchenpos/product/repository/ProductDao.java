package kitchenpos.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.id IN :ids  ")
    List<Product> findByIds(@Param("ids") final List<Long> ids);
}
