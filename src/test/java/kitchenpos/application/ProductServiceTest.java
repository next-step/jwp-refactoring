package kitchenpos.application;

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

    private Product iphone;
    private Product galaxy;

    @BeforeEach
    void setUp() {
        iphone = new Product(1L, "아이폰14", BigDecimal.valueOf(10000));
        galaxy = new Product(1L, "갤럭시", BigDecimal.valueOf(20000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        given(productDao.save(any())).willReturn(iphone);

        Product savedProduct = productService.create(iphone);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(iphone.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(iphone.getPrice())
        );
    }

    @DisplayName("가격이 존재하지 않는 상품은 등록할 수 없다.")
    @Test
    void createExceptionWithNull() {
        Product product = new Product(1L, "칸쵸", null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 음수인 상품은 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1000, -2000})
    void createExceptionWithNegative(int price) {
        Product product = new Product(1L, "칸쵸", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        given(productDao.findAll()).willReturn(Arrays.asList(iphone, galaxy));

        List<Product> products = productService.list();

        assertThat(products).hasSize(2);
        assertThat(products).contains(iphone, galaxy);
    }
}
