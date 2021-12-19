package kitchenpos.application;

import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Product;

@DisplayName("상품 통합 테스트")
class ProductServiceTest extends IntegrationTest {
	@Autowired
	private ProductService productService;

	@DisplayName("상품을 등록한다.")
	@Test
	void register() {
		// when
		Product product = productService.create(강정치킨_상품().toProduct());

		// then
		assertAll(
			() -> assertThat(product).isNotNull(),
			() -> assertThat(product.getId()).isNotNull());
	}

	@DisplayName("상품 가격이 음수일 경우 상품 등록에 실패한다.")
	@Test
	void registerFailOnNegativePrice() {
		// when
		ThrowableAssert.ThrowingCallable throwingCallable = () -> productService.create(음수가격_상품().toProduct());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("상품 이름이 없는 경우 상품 등록에 실패한다.")
	@Test
	void registerFailOnEmptyName() {
		// when
		ThrowableAssert.ThrowingCallable throwingCallable = () -> productService.create(이름없는_상품().toProduct());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("상품 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		Product 강정치킨_상품 = productService.create(강정치킨_상품().toProduct());
		Product 양념치킨_상품 = productService.create(양념치킨_상품().toProduct());

		// when
		List<Product> products = productService.list();

		// then
		List<Long> actualIds = products.stream()
			.map(Product::getId)
			.collect(Collectors.toList());
		List<Long> expectedIds = Stream.of(강정치킨_상품, 양념치킨_상품)
			.map(Product::getId)
			.collect(Collectors.toList());
		assertThat(actualIds).containsAll(expectedIds);
	}
}
