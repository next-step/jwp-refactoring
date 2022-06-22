package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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

   private Product 상품1;
   private Product 상품2;

    @BeforeEach
    void before() {
        상품1 = ProductFixtureFactory.create(1L, "상품1", BigDecimal.valueOf(1000));
        상품2 = ProductFixtureFactory.create(2L, "상품2", BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("생성하려는 상품에서 상품 가격 항목이 null이면 생성 할 수 없다.")
    void createFailWithNullTest() {
        //given
        Product 저장할_상품 = new Product(1L, "상품1", null);

        //when & then
        assertThatThrownBy(() -> productService.create(저장할_상품)).
                isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성하려는 상품에서 상품 가격이 음수라면 생성 할 수 없다.")
    void createFailWithPriceNegativeTest() {
        //given
        Product 저장할_상품 = new Product(1L, "상품1", BigDecimal.valueOf(-1));

        //when & then
        assertThatThrownBy(() -> productService.create(저장할_상품)).
                isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 생성 할 수 있다.")
    void createTest() {
        //given
        Product 저장할_상품 = new Product(1L, "상품1", BigDecimal.valueOf(1000));
        given(productDao.save(any(Product.class))).willReturn(상품1);

        //when
        Product product = productService.create(저장할_상품);

        //then
        assertThat(product).isEqualTo(상품1);
    }

    @Test
    @DisplayName("상품 목록을 조회 한다.")
    void listTest() {
        //given
        given(productDao.findAll()).willReturn(Arrays.asList(상품1, 상품2));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).containsExactly(상품1, 상품2);
    }
}
