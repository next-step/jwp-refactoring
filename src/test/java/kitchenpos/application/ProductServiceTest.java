package kitchenpos.application;

import static kitchenpos.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 감자튀김;
    private Product 콜라;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        when(productDao.save(감자튀김)).thenReturn(감자튀김);

        // when
        Product saveProduct = productService.create(감자튀김);

        // then
        assertAll(
                () -> assertThat(saveProduct.getId()).isNotNull(),
                () -> assertThat(saveProduct.getName()).isEqualTo(감자튀김.getName()),
                () -> assertThat(saveProduct.getPrice()).isEqualTo(감자튀김.getPrice())
        );
    }

    @DisplayName("가격이 비어있는 상품은 생성할 수 없다.")
    @Test
    void createProductThrowErrorWhenPriceIsNull() {
        // given
        Product product = generateProduct(3L, "감자튀김", null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("가격이 0원 미만인 상품은 생성할 수 없다.")
    @ParameterizedTest(name = "등록하고자 하는 상품의 가격: {0}")
    @ValueSource(longs = {-1000, -2000})
    void createProductThrowErrorWhenPriceIsNull(long price) {
        // given
        Product product = generateProduct(3L, "감자튀김", BigDecimal.valueOf(price));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 전체 목록을 조회한다.")
    @Test
    void findAllProducts() {
        // given
        List<Product> products = Arrays.asList(감자튀김, 콜라);
        when(productDao.findAll()).thenReturn(products);

        // when
        List<Product> findProducts = productService.list();

        // then
        assertAll(
                () -> assertThat(findProducts).hasSize(products.size()),
                () -> assertThat(findProducts).containsExactly(감자튀김, 콜라)
        );
    }
}
