package kitchenpos.product.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findAllByIds(List<Long> ids);

}
