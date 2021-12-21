package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	private static final String NAME = "로제치킨";
	private static final BigDecimal PRICE = new BigDecimal(23000);

	@Mock
	private ProductDao productDao;

	@Test
	@DisplayName("상품 생성 테스트")
	public void createProductSuccessTest() {
		//given
		Product product = new Product(null, NAME, PRICE);
		Mockito.when(productDao.save(product)).thenReturn(new Product(1L, NAME, PRICE));
		//when
		ProductService productService = new ProductService(productDao);
		Product createProduct = productService.create(product);
		//then
		assertThat(createProduct).isNotNull();
		assertThat(createProduct.getId()).isEqualTo(1L);
		assertThat(createProduct.getName()).isEqualTo(NAME);
		assertThat(createProduct.getPrice()).isEqualTo(PRICE);
	}


	@Test
	@DisplayName("상품가격이 0보다 작아서 생성 실패 테스트")
	public void createProductFailTest() {
		//given
		Product product = new Product();
		product.setName(NAME);
		product.setPrice(new BigDecimal(-1));

		//when
		ProductService productService = new ProductService(productDao);

		//then
		assertThatThrownBy(() -> productService.create(product))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("상품 가격은 0이상의 값을 가져야합니다.");
	}


	@Test
	@DisplayName("상품 조회 테스트")
	public void findProductListTest() {
		//given
		Product product = new Product(1L, NAME, PRICE);
		Product otherProduct = new Product(2L, NAME, PRICE);
		Mockito.when(productDao.findAll()).thenReturn(Lists.newArrayList(product, otherProduct));
		//when
		ProductService productService = new ProductService(productDao);
		List<Product> productList = productService.list();
		//then
		assertThat(productList).hasSize(2);
		assertThat(productList).containsExactly(product, otherProduct);
	}

}
