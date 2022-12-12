package kitchenpos.application;

import static kitchenpos.domain.ProductTestFixture.product;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("가격이 0보다 작은 상품을 등록한다.")
    void createProductByPriceLessThanZero() {
        // given
        ProductRequest 군만두 = new ProductRequest("군만두", BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> productService.create(군만두))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("가격이 없는 상품을 등록한다.")
    void createProductByPriceIsNull() {
        // given
        ProductRequest 군만두 = new ProductRequest("군만두", null);

        // when & then
        assertThatThrownBy(() -> productService.create(군만두))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 필수입니다.");
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void createProduct() {
        // given
        ProductRequest request = new ProductRequest("짜장면", BigDecimal.valueOf(8000));
        Product 짜장면 = request.toEntity();
        given(productRepository.save(짜장면)).willReturn(짜장면);

        // when
        ProductResponse actual = productService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("짜장면"),
                () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(8000))
        );
    }

    @Test
    @DisplayName("상품 목록을 조회하면 상품 목록이 반환된다.")
    void findProducts() {
        // given
        Product 짜장면 = product(1L, "짜장면", BigDecimal.valueOf(8000));
        Product 짬뽕 = product(2L, "짬뽕", BigDecimal.valueOf(9000));
        List<Product> products = Arrays.asList(짜장면, 짬뽕);
        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.stream()
                        .map(ProductResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly("짜장면", "짬뽕")
        );
    }
}
