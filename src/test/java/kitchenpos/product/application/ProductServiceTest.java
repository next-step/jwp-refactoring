package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Price;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	private ProductRequest chicken;
	private ProductRequest hotRamen;

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setUp() {
		chicken = new ProductRequest("치킨", 10000L);
		hotRamen = new ProductRequest("매운 라면", 10000L);
	}

	@DisplayName("상품을 등록한다.")
	@Test
	void createTestInHappyCase() {
		// given
		when(productRepository.save(any())).thenReturn(chicken);
		// when
		ProductResponse product = productService.create(chicken);
		// then
		assertThat(product.getName()).isEqualTo("치킨");
	}

	@DisplayName("상품 가격은 0보다 작을 수 없다.")
	@Test
	void createTestWithMinusPrice() {
		// given
		lenient().when(productRepository.save(any())).thenReturn(new ProductRequest("치킨", -10000L));
		// when, then
		assertThatThrownBy(() -> productService.create(new ProductRequest("치킨", -10000L)));
	}

	@DisplayName("상품을 조회한다.")
	@Test
	void listTestInHappyCase() {
		// given
		when(productRepository.findAll()).thenReturn(Arrays.asList(new Product("치킨", new Price(new BigDecimal(10000))), new Product("라면", new Price(new BigDecimal(10000)))));
		// when
		List<ProductResponse> products = productService.list();
		// then
		assertThat(products.size()).isEqualTo(2);
	}
}
