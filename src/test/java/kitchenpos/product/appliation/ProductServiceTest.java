package kitchenpos.product.appliation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal(10000);
    private static final String PRODUCT_NAME = "상품";
    private Product product = new Product();

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        product.setId(1L);
        product.setName(PRODUCT_NAME);
        product.setPrice(PRODUCT_PRICE);
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        when(productDao.save(any(Product.class)))
            .thenReturn(product);

        Product saved = productService.create(product);

        assertAll(() -> {
            assertThat(saved.getPrice()).isEqualTo(PRODUCT_PRICE);
            assertThat(saved.getName()).isEqualTo(PRODUCT_NAME);
        });
    }

    @Test
    @DisplayName("상품 생성 시 가격이 음수면 실패")
    void createNegativePriceFail() {
        product.setPrice(BigDecimal.valueOf(-1));
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 생성 시 가격 정보가 없는 경우 실패")
    void createNullPriceFail() {
        product.setPrice(null);
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 생성 시 이름 정보가 없는 경우 실패")
    void createNullNameFail() {
        // TODO: 2021/12/17 이름 정보 필수이므로 비지니스 로직 추가 필요. 현재는 실패하는 테스트임.
        product.setName(null);
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록 조회")
    void list() {
        when(productDao.findAll())
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(1);
    }
}