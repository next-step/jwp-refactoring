package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.fixture.ProductFixture;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;


    private Product 강정치킨;
    private Product 후라이드치킨;

    @BeforeEach
    void set_up() {
        후라이드치킨 = ProductFixture.create("후라이드치킨", BigDecimal.valueOf(15_000));
        강정치킨 = ProductFixture.create("강정치킨", BigDecimal.valueOf(18_000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        when(productRepository.save(any())).thenReturn(강정치킨);

        // when
        Product 상품_등록 = productService.create(강정치킨);

        // then
        assertThat(상품_등록).isEqualTo(강정치킨);
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @Test
    void create_price_fail() {
        // given
        Product 상품_가격_오류 = new Product(
                "name",
                BigDecimal.valueOf(-100_000)
        );

        // when && then
        assertThatThrownBy(() -> productService.create(상품_가격_오류))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void find_products() {
        // given
        when(productService.list()).thenReturn(Arrays.asList(강정치킨, 후라이드치킨));

        // when
        List<Product> 상품_목록 = productService.list();

        // then
        assertAll(
                () -> assertThat(상품_목록).hasSize(2),
                () -> assertThat(상품_목록).containsExactly(강정치킨, 후라이드치킨)
        );
    }

}
