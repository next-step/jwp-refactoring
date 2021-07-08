package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 관리")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 추가한다.")
    @Test
    void create() {
        //given
        Product product = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(10000));

        //and
        given(productDao.save(any())).willReturn(product);

        //when
        Product actual = productService.create(product);

        //then
        assertThat(actual.getId()).isEqualTo(product.getId());
        assertThat(actual.getName()).isEqualTo(product.getName());
        assertThat(actual.getPrice()).isEqualTo(product.getPrice());
    }

    @DisplayName("상품의 가격을 지정해야한다.")
    @Test
    void createProductExceptionIfPriceIsNull() {
        //given
        Product product = Product.of(1L, "후라이드치킨", null);

        //when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void createProductExceptionIfPriceIsNegative() {
        //given
        Product product = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(-1000));

        //when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("상품의 이름을 지정해야한다.")
    @Test
    void createProductExceptionIfNameIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("상품을 모두 조회한다.")
    @Test
    void list() {
        //given
        Product 후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(10000));
        Product 양념치킨 = Product.of(2L, "양념치킨", BigDecimal.valueOf(12000));
        List<Product> products = Lists.list(후라이드치킨, 양념치킨);

        //and
        given(productDao.findAll()).willReturn(products);

        //when
        List<Product> actual = productService.list();

        //then
        assertThat(actual.size()).isEqualTo(2);
        for (int i = 0; i < 2; i++) {
            assertThat(actual.get(i).getId()).isEqualTo(products.get(i).getId());
            assertThat(actual.get(i).getName()).isEqualTo(products.get(i).getName());
            assertThat(actual.get(i).getPrice()).isEqualTo(products.get(i).getPrice());
        }
    }
}
