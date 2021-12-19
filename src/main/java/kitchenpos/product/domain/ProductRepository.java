package kitchenpos.product.domain;

import kitchenpos.common.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product product(long id) {
        return findById(id).orElseThrow(
            () -> new NotFoundException(String.format("상품 id(%d)를 찾을 수 없습니다.", id)));
    }
}
