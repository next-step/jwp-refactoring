package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("상품 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private Product 후라이드치킨_상품;
    private Product 양념치킨_상품;
    private Product 간장치킨_상품;

    @BeforeEach
    void setUp() {
        후라이드치킨_상품 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000L));
        양념치킨_상품 = Product.of(2L, "양념치킨", BigDecimal.valueOf(16_000L));
        간장치킨_상품 = Product.of(3L, "간장치킨", BigDecimal.valueOf(17_000L));
    }

    @DisplayName("상품의 가격은 반드시 존재해야 한다.")
    @Test
    void 상품의_가격은_반드시_존재해야_한다() {
        // given
        후라이드치킨_상품.setPrice(null);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(후라이드치킨_상품));
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        // given
        후라이드치킨_상품.setPrice(new BigDecimal(-1));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(후라이드치킨_상품));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품을_생성한다() {
        // given
        when(productDao.save(후라이드치킨_상품)).thenReturn(후라이드치킨_상품);

        // when
        Product product = productService.create(후라이드치킨_상품);

        // then
        assertAll(() -> {
            assertThat(product.getId()).isEqualTo(1L);
            assertThat(product.getName()).isEqualTo("후라이드치킨");
            assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(16_000L));
        });
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void 상품을_조회한다() {
        // given
        when(productDao.findAll()).thenReturn(Arrays.asList(양념치킨_상품, 간장치킨_상품));

        // when
        List<Product> products = productService.list();

        // then
        assertAll(() -> {
            assertThat(products).hasSize(2);
            assertThat(products).containsExactly(양념치킨_상품, 간장치킨_상품);
        });
    }
}
