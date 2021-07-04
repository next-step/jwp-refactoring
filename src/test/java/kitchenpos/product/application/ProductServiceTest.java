package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

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
        Product productResponse = new Product(product1Id, product1Name, product1Price);

        when(productRepository.save(product1)).thenReturn(productResponse);

        assertThat(productService.create(product1)).isEqualTo(productResponse);
    }

    @DisplayName("상품 가격은 0 원 이상이어야 한다.")
    @Test
    void 상품_가격이_올바르지_않으면_등록할_수_없다_1() {
        BigDecimal falsePrice = BigDecimal.valueOf(-1);
        Product product1 = new Product(product1Id, product1Name, falsePrice);

        assertThatThrownBy(() -> {
            productService.create(product1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격은 없으면 안된다.")
    @Test
    void 상품_가격이_올바르지_않으면_등록할_수_없다_2() {
        BigDecimal falsePrice = null;
        Product product1 = new Product(product1Id, product1Name, falsePrice);

        assertThatThrownBy(() -> {
            productService.create(product1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체목록을 조회할 수 있다.")
    @Test
    void list() {
        Product productResponse = new Product(product1Id, product1Name, product1Price);

        when(productRepository.findAll()).thenReturn(Arrays.asList(productResponse));

        assertThat(productService.list()).contains(productResponse);
    }

}
