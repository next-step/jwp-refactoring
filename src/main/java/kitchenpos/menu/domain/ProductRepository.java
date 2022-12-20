package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	default Product product(long productId) {
		return findById(productId)
			.orElseThrow(() -> new IllegalArgumentException(String.format("상품 id(%d)를 찾을 수 없습니다.", productId)));
	}
}
