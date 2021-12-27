package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품을 등록한다")
	@Test
	void createTest() {
		// given
		ProductRequest.Create request = new ProductRequest.Create("강정치킨", new BigDecimal(17000));

		Product persist = Product.of(1L, "강정치킨", new BigDecimal(17000));

		given(productRepository.save(any())).willReturn(persist);

		// when
		ProductResponse result = productService.create(request);

		// then
		assertAll(
			() -> assertThat(result.getId()).isEqualTo(persist.getId()),
			() -> assertThat(result.getPrice()).isEqualTo(persist.getPrice().toBigDecimal()),
			() -> assertThat(result.getName()).isEqualTo(persist.getName().toText())
		);
	}

	@DisplayName("상품 가격이 0원 미만이면 등록이 안된다")
	@Test
	void createFailTest1() {
		// given
		ProductRequest.Create request = new ProductRequest.Create("강정치킨", new BigDecimal(-1));

		// when, then
		assertThatThrownBy(() -> productService.create(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("상품 목록을 조회한다")
	@Test
	void listTest() {
		// given
		List<Product> persist = new ArrayList<>();
		Product product1 = Product.of(1L, "후라이드", BigDecimal.ZERO);
		Product product2 = Product.of(2L, "양념", BigDecimal.ZERO);
		persist.add(product1);
		persist.add(product2);

		given(productRepository.findAll()).willReturn(persist);

		// when
		List<ProductResponse> result = productService.getList();

		// then
		assertThat(result.size()).isEqualTo(persist.size());
	}

}
