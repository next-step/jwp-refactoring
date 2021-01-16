package kitchenpos.mockito;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Disabled
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	private ProductService productService;

	@Mock
	private Product product;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository);
		assertThat(productService).isNotNull();
		product = mock(Product.class);
	}

	@Test
	@DisplayName("상품을 등록한다")
	void create() {
		given(product.getPrice()).willReturn(BigDecimal.valueOf(10000));
		given(productRepository.save(product)).willReturn(product);

//		assertThat(productService.create(product)).isEqualTo(product);
	}

	@Test
	@DisplayName("상품 목록을 조회한다")
	void list() {
		given(productRepository.findAll()).willReturn(new ArrayList<>(Arrays.asList(mock(Product.class))));

		assertThat(productService.list()).isNotNull();
		assertThat(productService.list()).isNotEmpty();
		assertThat(productService.list().size()).isEqualTo(1);
	}

}
