package ktichenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.generator.ProductGenerator;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.ui.request.ProductRequest;
import ktichenpos.menu.menu.application.ProductService;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void createProductTest() {
		// given
		ProductRequest 후라이드 = new ProductRequest("후라이드", BigDecimal.valueOf(16000));
		given(productRepository.save(any())).willReturn(ProductGenerator.상품("후라이드"));

		// when
		productService.create(후라이드);

		// then
		verify(productRepository, only()).save(any(Product.class));
	}

	@DisplayName("등록하려는 상품의 가격은 반드시 존재 해야 한다.")
	@Test
	void createProductWithNullPriceTest() {
		// given
		ProductRequest 후라이드 = new ProductRequest("후라이드", null);

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> productService.create(후라이드));
	}

	@DisplayName("상품의 가격은 0원 이상이어야 한다.")
	@Test
	void createProductWithNegativePriceTest() {
		// given
		ProductRequest 후라이드 = new ProductRequest("후라이드", BigDecimal.valueOf(-1));

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> productService.create(후라이드));
	}

	@DisplayName("상품의 목록을 조회할 수 있다.")
	@Test
	void listProductTest() {
		// when
		productService.list();

		// then
		verify(productRepository, only()).findAll();
	}
}
