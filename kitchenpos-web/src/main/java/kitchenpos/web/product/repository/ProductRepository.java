package kitchenpos.web.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.product.domain.Product;

/**
 * @author : byungkyu
 * @date : 2021/01/23
 * @description :
 **/
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
