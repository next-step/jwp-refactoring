package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.dao.ProductDao;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 돼지고기;
    private Product 공기밥;

    @BeforeEach
    void setUp() {
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
    }

    @DisplayName("Product 를 등록한다.")
    @Test
    void create1() {
        // given
        Product product = Product.of("돼지고기", BigDecimal.valueOf(9_000));

        given(productDao.save(any(Product.class))).willReturn(돼지고기);

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isEqualTo(돼지고기);
    }

    @DisplayName("Product 가격이 null 이면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Product product = Product.of("돼지고기", null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("Product 가격이 음수(0 미만) 이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int wrongPrice) {
        // given
        Product product = Product.of("돼지고기", BigDecimal.valueOf(wrongPrice));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("")
    @Test
    void findList() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(돼지고기, 공기밥));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(돼지고기, 공기밥);
    }
}