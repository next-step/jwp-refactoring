package kitchenpos.menu.application;

import kitchenpos.menu.application.exception.InvalidPrice;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private Product 매콤치킨;
    private Product 치즈볼;
    private Product 사이다;

    @BeforeEach
    void setUp() {
        매콤치킨 = new Product("매콤치킨", BigDecimal.valueOf(13000));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(2000));
        사이다 = new Product("사이다", BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        when(productRepository.save(any())).thenReturn(매콤치킨);

        ProductResponse response = productService.create(ProductRequest.of("매콤치킨", BigDecimal.valueOf(13000)));

        verify(productRepository, times(1)).save(any(Product.class));
        assertThat(response.getName()).isEqualTo(매콤치킨.getName());
    }

    @Test
    @DisplayName("상품의 가격이 null인 경우 예외가 발생한다.")
    void validatePriceNull() {
        ProductRequest request = ProductRequest.of("통통치킨", null);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(InvalidPrice.class);
    }

    @Test
    @DisplayName("상품의 가격이 0원 미만인 경우 예외가 발생한다.")
    void validateMinPrice() {
        BigDecimal invalidPrice = BigDecimal.valueOf(-1);
        ProductRequest request = ProductRequest.of("통통치킨", invalidPrice);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(InvalidPrice.class);
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void list() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(매콤치킨, 치즈볼, 사이다));

        List<ProductResponse> responses = productService.list();

        verify(productRepository, times(1)).findAll();
        assertThat(responses).hasSize(3);
    }
}