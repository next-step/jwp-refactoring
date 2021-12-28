package kitchenpos.menu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findAllByIds(List<Long> ids);

}
