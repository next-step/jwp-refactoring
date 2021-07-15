package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@DisplayName("상품 기능 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	private Product 물냉면;
	private Product 비빔냉면;

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private ProductService productService;

	@BeforeEach
	void setup() {
		물냉면 = new Product();
		물냉면.setId(1L);
		물냉면.setName("물냉면");
		물냉면.setPrice(new BigDecimal(7000));

		비빔냉면 = new Product();
		비빔냉면.setId(2L);
		비빔냉면.setName("비빔냉면");
		비빔냉면.setPrice(new BigDecimal(7000));
	}

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	public void create() {
		// given
		given(productDao.save(물냉면)).willReturn(물냉면);

		// when
		Product createdProduct = productService.create(물냉면);

		// then
		assertThat(createdProduct.getId()).isEqualTo(물냉면.getId());
		assertThat(createdProduct.getName()).isEqualTo(물냉면.getName());
		assertThat(createdProduct.getPrice()).isEqualTo(물냉면.getPrice());
	}

	@DisplayName("상품의 가격은 0 원 이상이어야 한다.")
	@Test
	public void createInvalidPrice() {
		// given
		물냉면.setPrice(new BigDecimal(-1));

		assertThrows(IllegalArgumentException.class, () -> {
			productService.create(물냉면);
		});
	}

	@DisplayName("상품의 목록을 조회할 수 있다.")
	@Test
	public void list() {
		// given
		given(productDao.findAll()).willReturn(Arrays.asList(물냉면, 비빔냉면));

		// when
		List<Product> products = productService.list();

		// then
		assertThat(products).containsExactly(물냉면, 비빔냉면);
	}
}
