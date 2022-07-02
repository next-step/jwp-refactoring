package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductDao productDao;

    private Product 상품;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품 생성")
    @Test
    void create() {
        // given
        상품 = new Product(1L, "상품", BigDecimal.valueOf(3000));
        given(productDao.save(any())).willReturn(상품);

        // when
        Product product = productService.create(상품);

        // then
        assertThat(product).isEqualTo(상품);
    }

    @DisplayName("상품 조회")
    @Test
    void list() {
        // given
        상품 = new Product(1L, "상품", BigDecimal.valueOf(3000));
        given(productDao.findAll()).willReturn(Collections.singletonList(상품));

        // when
        List<Product> products = productService.list();

        // then
        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(products).containsExactly(상품)
        );
    }

    @Test
    void 상품_가격이_0보다_작은경우() {
        // given
        상품 = new Product(1L, "상품", BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> productService.create(상품))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격이_빈값인_경우() {
        // given
        상품 = new Product(1L, "상품", null);

        // when & then
        assertThatThrownBy(() -> productService.create(상품))
                .isInstanceOf(IllegalArgumentException.class);
    }
}