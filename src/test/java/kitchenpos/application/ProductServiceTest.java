package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

/**
 * @author : byungkyu
 * @date : 2021/01/19
 * @description :
 **/
@DisplayName("상품")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@Mock
	private ProductDao productDao;

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create(){
		// given
		Product product = new Product("후라이드", BigDecimal.valueOf(16000));
		when(productDao.save(product)).thenReturn(new Product(1L, "후라이드", BigDecimal.valueOf(16000)));
		ProductService productService = new ProductService(productDao);

		// when
		Product savedProduct = productService.create(product);

		// then
		assertThat(savedProduct.getId()).isNotNull();
		assertThat(savedProduct.getName()).isEqualTo(product.getName());
		assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
	}

	@DisplayName("상품의 가격은 0원 이상이어야 한다.")
	@Test
	void priceMustOverZero(){
		// given
		Product product = new Product("후라이드", BigDecimal.valueOf(0));
		when(productDao.save(product)).thenThrow(RuntimeException.class);
		ProductService productService = new ProductService(productDao);

		// when then
		assertThatThrownBy(() -> {
			productService.create(product);

		}).isInstanceOf(RuntimeException.class);
	}

	@DisplayName("상품의 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		Product 후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
		when(productDao.findAll()).thenReturn(Arrays.asList(후라이드));
		ProductService productService = new ProductService(productDao);

		//when
		List<Product> products = productService.list();

		//then
		assertThat(products).containsExactly(후라이드);
	}

}