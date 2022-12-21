package kitchenpos.menu.dao;

import java.util.List;
import kitchenpos.menu.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {

    List<Product> findProductByIdIn(List<Long> productIds);
}
