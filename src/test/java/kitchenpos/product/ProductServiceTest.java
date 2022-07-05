package kitchenpos.product;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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

import static kitchenpos.util.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    private Product 후라이드;
    private Product 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드 = 후라이드_상품_생성();
        양념치킨 = 양념치킨_상품_생성();
    }

    @DisplayName("상품 등록")
    @Test
    void createProduct() {
        // given
        when(productRepository.save(any()))
                .thenReturn(후라이드);

        // when
        ProductResponse result = productService.create(new ProductRequest(후라이드.getName(), 후라이드.getPrice()));

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(후라이드.getId()),
                () -> assertThat(result.getName()).isEqualTo(후라이드.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(후라이드.getPrice())
        );
    }

    @DisplayName("상품 가격이 0보다 작은 경우 등록 불가")
    @Test
    void createProductAndPriceZero() {
        // given
        ProductRequest 후라이드_요청 = new ProductRequest(후라이드.getName(), new BigDecimal(-1));

        // then
        assertThatThrownBy(() -> {
            productService.create(후라이드_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 `null`인 경우 등록 불가")
    @Test
    void createProductAndPriceNull() {
        // given
        ProductRequest 후라이드_요청 = new ProductRequest(후라이드.getName(), null);

        // then
        assertThatThrownBy(() -> {
            productService.create(후라이드_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체 조회")
    @Test
    void findProducts() {
        // given
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(후라이드, 양념치킨));

        // when
        List<ProductResponse> list = productService.list();

        // then
        assertThat(list.size()).isEqualTo(2);
    }
}
