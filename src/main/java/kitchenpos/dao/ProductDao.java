package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long>{
    List<Product> findByIdIn(List<Long> id);
}
