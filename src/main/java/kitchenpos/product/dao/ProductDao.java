package kitchenpos.product.dao;

import java.util.List;
import kitchenpos.product.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {

    List<Product> findProductByIdIn(List<Long> productIds);
}
