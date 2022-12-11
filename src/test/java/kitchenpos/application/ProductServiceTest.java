package kitchenpos.application;

import static kitchenpos.domain.ProductTestFixture.product;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("가격이 0보다 작은 상품을 등록한다.")
    void createProductByPriceLessThanZero() {
        // given
        Product 군만두 = product(3L, "군만두", BigDecimal.valueOf(-1000));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(군만두));
    }

    @Test
    @DisplayName("가격이 없는 상품을 등록한다.")
    void createProductByPriceIsNull() {
        // given
        Product 군만두 = product(3L, "군만두", null);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(군만두));
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void createProduct() {
        // given
        Product 짜장면 = product(1L, "짜장면", BigDecimal.valueOf(8000));
        given(productDao.save(짜장면)).willReturn(짜장면);

        // when
        Product actual = productService.create(짜장면);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Product.class)
        );
    }

    @Test
    @DisplayName("상품 목록을 조회하면 상품 목록이 반환된다.")
    void findProducts() {
        // given
        Product 짜장면 = product(1L, "짜장면", BigDecimal.valueOf(8000));
        Product 짬뽕 = product(2L, "짬뽕", BigDecimal.valueOf(9000));
        List<Product> products = Arrays.asList(짜장면, 짬뽕);
        given(productDao.findAll()).willReturn(products);

        // when
        List<Product> actual = productService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(짜장면, 짬뽕)
        );
    }
}
