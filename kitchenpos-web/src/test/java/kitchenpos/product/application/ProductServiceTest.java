package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/19
 * @description :
 **/
@DisplayName("상품")
class ProductServiceTest extends IntegrationTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@AfterEach
	void cleanUp() {
		productRepository.deleteAllInBatch();
	}

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create(){
		// given
		ProductRequest request = new ProductRequest("마늘치킨", BigDecimal.valueOf(16000));

		// when
		ProductResponse savedProduct = productService.create(request);

		// then
		assertThat(savedProduct.getId()).isNotNull();
		assertThat(savedProduct.getName()).isEqualTo(request.getName());
		assertThat(savedProduct.getPrice()).isEqualByComparingTo(request.getPrice());
	}

	@DisplayName("상품의 가격은 0원 이상이어야 한다.")
	@Test
	void priceMustOverZero(){
		// given
		ProductRequest request = new ProductRequest("마늘치킨", BigDecimal.valueOf(-500));
		// when then
		assertThatThrownBy(() -> {
			productService.create(request);

		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품의 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		ProductRequest request = new ProductRequest("마늘치킨", BigDecimal.valueOf(16000));
		ProductResponse productResponse = productService.create(request);

		// when
		List<ProductResponse> products = productService.list();
		List<Long> productNames = products.stream()
			.map(product -> product.getId())
			.collect(Collectors.toList());

		//then
		assertThat(products).isNotEmpty();
		assertThat(productNames).contains(productResponse.getId());
	}

}