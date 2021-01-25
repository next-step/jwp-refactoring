package kitchenpos.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : byungkyu
 * @date : 2021/01/23
 * @description :
 **/
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
