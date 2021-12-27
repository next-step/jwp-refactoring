package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.common.Price;

@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("상품 저장")
	public void save() {
		//given
		Product product = new Product(null, "로제치킨", new Price(new BigDecimal(15000)));
		//when
		Product save = productRepository.save(product);
		//then
		assertThat(save).isEqualTo(new Product(7L, "로제치킨", new Price(new BigDecimal(15000))));
	}

	@Test
	@DisplayName("상품 목록 조회")
	public void findAll() {
		//given
		//when
		List<Product> products = productRepository.findAll();
		//then
		assertThat(products).hasSize(6);
	}

	@Test
	@DisplayName("상품 ID로 상품 조회")
	public void findById() {
		//given
		//when
		Product product = productRepository.findById(3L).get();
		//then
		assertThat(product.getId()).isEqualTo(3L);
	}
}
