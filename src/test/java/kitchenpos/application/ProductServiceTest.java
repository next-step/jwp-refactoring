package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create_product() {
        // given
        Product 강정치킨 = create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        when(productDao.save(any())).thenReturn(강정치킨);

        // when
        Product product = productService.create(강정치킨);

        // then
        assertThat(product).isEqualTo(강정치킨);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void create_price_fail() {
        // given
        Product 강정치킨 = create(1L, "강정치킨", BigDecimal.valueOf(-10_000));

        // when && then
        assertThatThrownBy(() -> productService.create(강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void find_products() {
        // given
        Product 강정치킨 = create(1L, "강정치킨", BigDecimal.valueOf(17_000));
        Product 후라이드치킨 = create(2L, "후라이드치킨", BigDecimal.valueOf(15_000));

        // when
        when(productService.list()).thenReturn(Arrays.asList(강정치킨, 후라이드치킨));
        List<Product> 상품_목록 = productService.list();

        // then
        assertAll(
                () -> assertThat(상품_목록).hasSize(2),
                () -> assertThat(상품_목록).containsExactly(강정치킨, 후라이드치킨)
        );
    }

    private static Product create(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
