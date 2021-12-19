package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("상품 통합 테스트")
class ProductServiceTest extends IntegrationTest {
	private static final String NAME = "강정치킨";
	private static final BigDecimal PRICE = BigDecimal.valueOf(17000L);

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductDao productDao;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create() {
		// given
		Product request = new Product();
		request.setName(NAME);
		request.setPrice(PRICE);

		// when
		Product product = productService.create(request);

		// then
		assertAll(
			() -> assertThat(product.getId()).isNotNull(),
			() -> assertThat(product.getName()).isEqualTo(request.getName()),
			() -> assertThat(product.getPrice().toBigInteger()).isEqualTo(request.getPrice().toBigInteger()));
	}

	@DisplayName("상품 이름이 빈 값이면 등록할 수 없다.")
	@ParameterizedTest
	@NullSource
	void createFailOnEmptyName(String name) {
		// given
		Product request = new Product();
		request.setName(name);
		request.setPrice(PRICE);

		// when
		ThrowableAssert.ThrowingCallable throwingCallable = () -> productService.create(request);

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("상품 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		Product request = new Product();
		request.setName(NAME);
		request.setPrice(PRICE);

		Product given = productDao.save(request);

		// when
		List<Product> actual = productService.list();

		// then
		List<Long> actualIds = actual.stream().map(Product::getId).collect(Collectors.toList());

		assertAll(
			() -> assertThat(actual).isNotEmpty(),
			() -> assertThat(actualIds).contains(given.getId())
		);
	}

}
