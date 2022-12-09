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

import static kitchenpos.fixture.ProductFixture.강정치킨;
import static kitchenpos.fixture.ProductFixture.후라이드치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create_product() {
        // given && when
        when(productDao.save(any())).thenReturn(강정치킨);
        Product product = productService.create(강정치킨);

        // then
        assertThat(product).isEqualTo(강정치킨);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void create_price_fail() {
        // given
        Product 상품_가격_오류 = new Product();
        상품_가격_오류.setPrice(BigDecimal.valueOf(-100_000));

        // when && then
        assertThatThrownBy(() -> productService.create(상품_가격_오류))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void find_products() {
        // given &&  when
        when(productService.list()).thenReturn(Arrays.asList(강정치킨, 후라이드치킨));
        List<Product> 상품_목록 = productService.list();

        // then
        assertAll(
                () -> assertThat(상품_목록).hasSize(2),
                () -> assertThat(상품_목록).containsExactly(강정치킨, 후라이드치킨)
        );
    }

}
