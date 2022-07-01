package kitchenpos.product.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        Product product = 상품_생성("후라이드치킨", 15_000);

        when(productRepository.save(product)).thenReturn(상품_생성(1L, "후라이드치킨", 15_000));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice())
        );
    }

    @DisplayName("가격은 0 이상이여아 한다.")
    @Test
    void createProduct1() {
        // given
        Product product = 상품_생성("후라이드치킨", -1_000);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void listProduct() {
        // given
        Product product1 = 상품_생성("후라이드치킨", 15_000);
        Product product2 = 상품_생성("양념치킨", 15_000);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(product1, product2);
    }
}
