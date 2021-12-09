package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.*;
import static kitchenpos.application.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        when(productDao.save(any(Product.class))).thenReturn(돼지고기);
    }

    @DisplayName("Product 를 등록한다.")
    @Test
    void create1() {
        // given
        Product product = new Product();
        product.setName("돼지고기");
        product.setPrice(BigDecimal.valueOf(9_000));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct).isEqualTo(돼지고기);
    }

    @DisplayName("Product 가격이 null 이면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Product product = new Product();
        product.setName("돼지고기");
        product.setPrice(null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("Product 가격이 음수(0 미만) 이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10, -100})
    void create3(int wrongPrice) {
        // given
        Product product = new Product();
        product.setName("돼지고기");
        product.setPrice(BigDecimal.valueOf(wrongPrice));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("")
    @Test
    void findList() {
        // given
        when(productDao.findAll()).thenReturn(Arrays.asList(돼지고기, 공기밥));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(돼지고기, 공기밥);
    }
}