package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("상품 : 서비스 테스트")
@ExtendWith({MockitoExtension.class})
class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	private Product product;

	@BeforeEach
	void setup() {
		product = new Product();
	}

	@DisplayName("상품 생성 테스트")
	@Test
	void createTest() {
		// given
		product.setId(1L);
		product.setName("후라이드");
		product.setPrice(new BigDecimal(16000));

		// when
		when(productDao.save(product)).thenReturn(product);

		// then
		assertThat(productService.create(product)).isEqualTo(product);
	}

	@DisplayName("상품의 가격이 없는 경우 생성시 예외처리 테스트")
	@Test
	void createPriceNull() {
		// given
		product.setId(1L);
		product.setName("후라이드");

		// when // then
		assertThatThrownBy(() -> {
			productService.create(product);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품의 가격이 0보다 작은 경우 생성시 예외처리 테스트")
	@Test
	void createPriceUnderZero() {
		// given
		product.setId(1L);
		product.setName("후라이드");
		product.setPrice(new BigDecimal(-100));

		// when // then
		assertThatThrownBy(() -> {
			productService.create(product);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 목록 조회 테스트")
	@Test
	void getList() {
		// given
		product.setId(1L);
		product.setName("후라이드");
		product.setPrice(new BigDecimal(16000));
		Product anotherProduct = new Product();
		anotherProduct.setId(2L);
		anotherProduct.setName("양념치킨");
		anotherProduct.setPrice(new BigDecimal(16000));

		// when
		when(productDao.findAll()).thenReturn(Arrays.asList(product, anotherProduct));

		// then
		assertThat(productService.list()).containsExactly(product, anotherProduct);
	}
}
