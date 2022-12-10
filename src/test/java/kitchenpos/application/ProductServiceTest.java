package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private Product 치킨;
    private Product 치즈볼;

    @BeforeEach
    void setUp() {
        치킨 = Product.of(1L, "치킨", BigDecimal.valueOf(3000L));
        치즈볼 = Product.of(2L, "치즈볼", BigDecimal.valueOf(1500L));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() {
        // given
        when(productDao.save(치킨)).thenReturn(치킨);

        // when
        Product saveProduct = productService.create(치킨);

        // then
        assertAll(
                () -> assertThat(saveProduct.getId()).isNotNull(),
                () -> assertThat(saveProduct.getName()).isEqualTo(치킨.getName()),
                () -> assertThat(saveProduct.getPrice()).isEqualTo(치킨.getPrice())
        );
    }

    @DisplayName("가격이 없는 상품은 생성할 수 없다.")
    @Test
    void 가격이_없는_상품_생성() {
        // given
        Product product = Product.of(3L, "콜라", null);

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("가격이 0원 미만인 상품은 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 상품의 가격: {0}")
    @ValueSource(longs = {-5, -100})
    void 가격이_음수인_상품_생성(long price) {
        // given
        Product product = Product.of(3L, "감자튀김", BigDecimal.valueOf(price));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록_조회() {
        // given
        List<Product> products = Arrays.asList(치킨, 치즈볼);
        when(productDao.findAll()).thenReturn(products);

        // when
        List<Product> findProducts = productService.list();

        // then
        assertAll(
                () -> assertThat(findProducts).hasSize(products.size()),
                () -> assertThat(findProducts).containsExactly(치킨, 치즈볼)
        );
    }
}
