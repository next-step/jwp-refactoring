package kitchenpos.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("상품 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private Product 삼겹살;
    private Product 소고기;

    @BeforeEach
    void setUp() {
        삼겹살 = new Product("삼겹살", BigDecimal.valueOf(3000L));
        소고기 = new Product("소고기", BigDecimal.valueOf(1500L));
    }

    @DisplayName("상품 생성 테스트")
    @Test
    void createProductTest() {
        when(productRepository.save(삼겹살)).thenReturn(삼겹살);

        // when
        ProductResponse saveProduct = productService.create(new ProductRequest(삼겹살.getName(), 삼겹살.getPrice()));

        // then
        assertAll(
                () -> assertThat(saveProduct.getName()).isEqualTo(삼겹살.getName()),
                () -> assertThat(saveProduct.getPrice()).isEqualTo(삼겹살.getPrice())
        );
    }

    private void checkForCreateProduct(Product createProduct, Product sourceProduct) {
        assertAll(
                () -> assertThat(createProduct.getId()).isEqualTo(sourceProduct.getId()),
                () -> assertThat(createProduct.getName()).isEqualTo(sourceProduct.getName()),
                () -> assertThat(createProduct.getPrice()).isEqualTo(sourceProduct.getPrice())
        );
    }

    @DisplayName("상품 생성 테스트 - 올바르지 않는 가격인경우")
    @ParameterizedTest
    @ValueSource(ints = { -1, -1000 })
    void createProductTest2(int price) {
        // when & then
        assertThatThrownBy(() -> new Product(1L, "삼겹살", BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void findAllProductsTest() {
        // given
        List<Product> products = Arrays.asList(삼겹살, 소고기);
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<ProductResponse> findProducts = productService.list();

        // then
        assertAll(
                () -> assertThat(findProducts).hasSize(products.size()),
                () -> assertThat(findProducts.stream().map(ProductResponse::getName).collect(Collectors.toList()))
                        .containsExactly(삼겹살.getName(), 소고기.getName())
        );
    }

}
