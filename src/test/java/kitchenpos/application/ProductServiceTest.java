package kitchenpos.application;

import static kitchenpos.domain.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 후라이드치킨;
    private Product 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = createProduct(1L, "후라이드치킨", BigDecimal.valueOf(10000));
        양념치킨 = createProduct(1L, "양념치킨", BigDecimal.valueOf(20000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        given(productDao.save(any())).willReturn(후라이드치킨);

        Product savedProduct = productService.create(후라이드치킨);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(후라이드치킨.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(후라이드치킨.getPrice())
        );
    }

    @DisplayName("가격이 존재하지 않는 상품은 등록할 수 없다.")
    @Test
    void createExceptionWithNull() {
        Product product = createProduct(1L, "치킨무", null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수인 상품은 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1000, -2000})
    void createExceptionWithNegative(int price) {
        Product product = createProduct(1L, "치킨무", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(productDao.findAll()).willReturn(Arrays.asList(후라이드치킨, 양념치킨));

        List<Product> products = productService.list();

        assertThat(products).hasSize(2);
        assertThat(products).contains(후라이드치킨, 양념치킨);
    }
}
