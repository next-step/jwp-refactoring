package kitchenpos;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("상품 BO 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품 생성")
	@Test
	void create_happyPath() {
		// given
		ProductRequest 새_상품_요청 = new ProductRequest("새상품", BigDecimal.valueOf(4000L));

		given(productDao.save(any(Product.class))).willAnswer(invocation -> {
			Product mock = spy(invocation.getArgument(0, Product.class));
			when(mock.getId()).thenReturn(1L);
			return mock;
		});

		// when
		ProductResponse response = productService.create(새_상품_요청);

		// then
		assertAll(
			() -> assertThat(response.getId()).isEqualTo(1L),
			() -> assertThat(response.getName()).isEqualTo(새_상품_요청.getName()),
			() -> assertThat(response.getPrice()).isEqualTo(새_상품_요청.getPrice().longValue())
		);
	}

	@DisplayName("상품 생성 : 가격이 0원보다 작음")
	@Test
	void create_exceptionCase() {
		// given
		ProductRequest 새_상품_요청 = new ProductRequest("새상품", BigDecimal.valueOf(-1L));

		// when & then
		assertThatThrownBy(() -> productService.create(새_상품_요청)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 목록 조회")
	@Test
	void list() {
		// given
		given(productDao.findAll()).willReturn(Arrays.asList(상품1, 상품2));

		// when
		List<ProductResponse> listResponse = productService.list();

		// then
		assertThat(listResponse)
			.hasSize(2)
			.anySatisfy(productResponse -> {
				assertThat(productResponse.getId()).isEqualTo(상품1.getId());
				assertThat(productResponse.getName()).isEqualTo(상품1.getName());
				assertThat(productResponse.getPrice()).isEqualTo(상품1.getPrice().longValue());
			})
			.anySatisfy(productResponse -> {
				assertThat(productResponse.getId()).isEqualTo(상품2.getId());
				assertThat(productResponse.getName()).isEqualTo(상품2.getName());
				assertThat(productResponse.getPrice()).isEqualTo(상품2.getPrice().longValue());
			});
	}
}
