package kitchenpos.application;

import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("상품 단위 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@Mock
	private ProductDao productDao;
	@InjectMocks
	private ProductService productService;

	@DisplayName("상품을 등록한다.")
	@Test
	void register() {
		// given
		given(productDao.save(any())).willReturn(강정치킨_상품());

		// when
		Product product = productService.create(강정치킨_상품_요청().toProduct());

		// then
		assertAll(
			() -> assertThat(product).isNotNull(),
			() -> assertThat(product.getId()).isNotNull());
	}

	@DisplayName("상품 가격이 음수일 경우 상품 등록에 실패한다.")
	@Test
	void registerFailOnNegativePrice() {
		// when
		ThrowingCallable throwingCallable = () -> productService.create(음수가격_상품_요청().toProduct());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("상품 이름이 없는 경우 상품 등록에 실패한다.")
	@Test
	void registerFailOnEmptyName() {
		// given
		given(productDao.save(any())).willThrow(RuntimeException.class);

		// when
		ThrowingCallable throwingCallable = () -> productService.create(이름없는_상품_요청().toProduct());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("상품 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		given(productDao.findAll()).willReturn(Arrays.asList(강정치킨_상품(), 양념치킨_상품()));

		// when
		List<Product> products = productService.list();

		// then
		List<Long> actualIds = products.stream()
			.map(Product::getId)
			.collect(Collectors.toList());
		List<Long> expectedIds = Stream.of(강정치킨_상품(), 양념치킨_상품())
			.map(Product::getId)
			.collect(Collectors.toList());
		assertThat(actualIds).containsAll(expectedIds);
	}
}
