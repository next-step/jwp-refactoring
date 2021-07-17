package kitchenpos.application;

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

import kitchenpos.product.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.product.dto.ProductRequest;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	private ProductRequest chicken;
	private ProductRequest hotRamen;

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setUp() {
		chicken = new ProductRequest("치킨", new BigDecimal(10000));
		hotRamen = new ProductRequest("매운 라면", new BigDecimal(10000));
	}

	@DisplayName("상품을 등록한다.")
	@Test
	void createTestInHappyCase() {
		// given
		when(productDao.save(any())).thenReturn(chicken);
		// when
		ProductRequest product = productService.create(chicken);
		// then
		assertThat(product.getName()).isEqualTo("치킨");
	}

	@DisplayName("상품 가격은 0보다 작을 수 없다.")
	@Test
	void createTestWithMinusPrice() {
		// given
		lenient().when(productDao.save(any())).thenReturn(new ProductRequest("치킨", new BigDecimal(-10000)));
		// when, then
		assertThatThrownBy(() -> productService.create(new ProductRequest("치킨", new BigDecimal(-10000))));
	}

	@DisplayName("상품을 조회한다.")
	@Test
	void listTestInHappyCase() {
		// given
		when(productDao.findAll()).thenReturn(Arrays.asList(chicken, hotRamen));
		// when
		List<ProductRequest> products = productService.list();
		// then
		assertThat(products.size()).isEqualTo(2);
	}
}
