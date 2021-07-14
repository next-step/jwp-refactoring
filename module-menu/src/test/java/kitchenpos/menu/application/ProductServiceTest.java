package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long product1Id = 1L;
    private String product1Name = "상품1";
    private BigDecimal product1Price = BigDecimal.valueOf(1000);

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        Product product1 = new Product(product1Id, product1Name, product1Price);
        ProductRequest productRequest = ProductRequest.of(product1Name, product1Price);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product1);

        ProductResponse productResponse = productService.create(productRequest);

        assertThat(productResponse.getId()).isEqualTo(product1.getId());
        assertThat(productResponse.getName()).isEqualTo(product1.getName());
        assertThat(productResponse.getPrice()).isEqualTo(product1.getPrice());
    }

    @DisplayName("상품 가격은 0 원 이상이어야 한다.")
    @Test
    void 상품_가격이_올바르지_않으면_등록할_수_없다_1() {
        BigDecimal falsePrice = BigDecimal.valueOf(-1);
        ProductRequest productRequest = ProductRequest.of(product1Name, falsePrice);

        Assertions.assertThatThrownBy(() -> {
            productService.create(productRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격은 없으면 안된다.")
    @Test
    void 상품_가격이_올바르지_않으면_등록할_수_없다_2() {
        BigDecimal falsePrice = null;
        ProductRequest productRequest = ProductRequest.of(product1Name, falsePrice);

        Assertions.assertThatThrownBy(() -> {
            productService.create(productRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체목록을 조회할 수 있다.")
    @Test
    void list() {
        Product product = new Product(product1Id, product1Name, product1Price);

        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        assertThat(productService.list()).contains(ProductResponse.of(product));
    }

}
