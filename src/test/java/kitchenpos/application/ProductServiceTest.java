package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
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
import java.util.List;

import static kitchenpos.fixtures.ProductFixtures.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : ProductServiceTest
 * author : haedoang
 * date : 2021/12/16
 * description :
 */
@DisplayName("상품 비즈니스 오브젝트 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private Product product;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        product = createProduct(1L, "고추바사삭치킨", new BigDecimal(18000));
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    public void create() {
        // given
        given(productDao.save(any(Product.class))).willReturn(product);

        // when
        Product actual = productService.create(product);

        // then
        assertThat(actual).isEqualTo(product);
    }

    @ParameterizedTest(name = "value: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {-10, -5, -1})
    @DisplayName("상품의 가격은 0 원 이상이어야 한다: int")
    public void createFail(int candidate) {
        // when
        product.setPrice(new BigDecimal(candidate));

        // then
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("제품 등록 시 유효하지 않은 가격은 등록할 수 없다: null")
    public void createFail2() {
        //when
        product.setPrice(null);

        // then
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다.")
    public void list() {
        // given
        given(productDao.findAll()).willReturn(Lists.newArrayList(product));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(1);
    }
}