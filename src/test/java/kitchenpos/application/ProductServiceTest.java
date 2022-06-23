package kitchenpos.application;

import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;
    private Product 피자;
    private Product 스파게티;

    @BeforeEach
    void setUp() {
        피자 = createProduct(1L, "피자", BigDecimal.valueOf(20000L));
        스파게티 = createProduct(2L, "스파게티", BigDecimal.valueOf(20000L));
    }

    @DisplayName("상품 생성 테스트")
    @Test
    void create() {
        given(productDao.save(피자)).willReturn(피자);
        Product product = productService.create(피자);
        assertAll(
                () -> assertThat(product.getName()).isEqualTo("피자"),
                () -> assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(20000L))
        );
    }

    @DisplayName("상품 생성시 가격이 없는 경우 테스트")
    @Test
    void createWithPriceNull() {
        Product product = createProduct(1L, "스파게티", null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 생성시 가격이 0원 아래인 경우 테스트")
    @Test
    void createWithPriceUnderZero() {
        Product product = createProduct(1L, "스파게티", BigDecimal.valueOf(-100L));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 목록 조회 테스트")
    @Test
    void list() {
        given(productDao.findAll()).willReturn(Lists.newArrayList(피자, 스파게티));
        List<Product> products = productService.list();
        assertThat(products).containsExactlyElementsOf(Lists.newArrayList(피자, 스파게티));
    }
}
