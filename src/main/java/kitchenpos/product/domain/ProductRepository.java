package kitchenpos.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface ProductRepository extends JpaRepository<Product, Long> {
    default Product getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다. id: " + id));
    }
}
