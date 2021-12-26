package kitchenpos.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName : kitchenpos.domain
 * fileName : ProductRepository
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
