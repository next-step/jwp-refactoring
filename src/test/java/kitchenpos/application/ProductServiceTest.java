package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
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

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductTest;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@DisplayName("상품을 등록한다")
	@Test
	void createTest() {
		// given
		Product product = new Product();
		product.setName("강정치킨");
		product.setPrice(new BigDecimal(17000));
		given(productDao.save(any())).willReturn(product);

		// when
		Product result = productService.create(product);

		// then
		assertThat(result.getPrice()).isEqualTo(product.getPrice());
		assertThat(result.getName()).isEqualTo(product.getName());
	}

	@DisplayName("상품 가격이 0원 미만이면 등록이 안된다")
	@Test
	void createFailTest1() {
		// given
		Product product = new Product();
		product.setName("강정치킨");
		product.setPrice(new BigDecimal(-1));

		// when, then
		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 목록을 조회한다")
	@Test
	void listTest() {
		// given
		List<Product> products = new ArrayList<>();
		products.add(ProductTest.후라이드);
		products.add(ProductTest.간장치킨);

		given(productDao.findAll()).willReturn(products);

		// when
		List<Product> result = productService.list();

		// then
		assertThat(result).containsAll(products);
	}

}
