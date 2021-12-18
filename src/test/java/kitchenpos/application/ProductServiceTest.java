package kitchenpos.application;

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

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

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
		Product request = new Product();
		request.setName("강정치킨");
		request.setPrice(new BigDecimal(17000));

		Product persist = new Product();
		persist.setId(1L);
		persist.setName("강정치킨");
		persist.setPrice(new BigDecimal(17000));

		given(productDao.save(any())).willReturn(persist);

		// when
		Product result = productService.create(request);

		// then
		assertAll(
			() -> assertThat(result.getId()).isEqualTo(persist.getId()),
			() -> assertThat(result.getPrice()).isEqualTo(persist.getPrice()),
			() -> assertThat(result.getName()).isEqualTo(persist.getName())
		);
	}

	@DisplayName("상품 가격이 0원 미만이면 등록이 안된다")
	@Test
	void createFailTest1() {
		// given
		Product request = new Product();
		request.setName("강정치킨");
		request.setPrice(new BigDecimal(-1));

		// when, then
		assertThatThrownBy(() -> productService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 목록을 조회한다")
	@Test
	void listTest() {
		// given
		List<Product> persist = new ArrayList<>();
		Product product1 = new Product();
		product1.setId(1L);
		Product product2 = new Product();
		product1.setId(2L);
		persist.add(product1);
		persist.add(product2);

		given(productDao.findAll()).willReturn(persist);

		// when
		List<Product> result = productService.list();

		// then
		assertThat(result).containsAll(persist);
	}

}
