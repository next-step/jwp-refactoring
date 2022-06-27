package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.UnitTestFixture;
import org.junit.jupiter.api.BeforeEach;
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

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 상품을_등록할_수_있어야_한다() {
        // given
        final Product given = new Product(1L, "new product", BigDecimal.valueOf(15000L));
        when(productDao.save(any(Product.class))).thenReturn(given);

        // when
        final Product actual = productService.create(given);

        // then
        assertThat(given).isEqualTo(actual);
    }

    @Test
    void 상품_목록을_조회할_수_있어야_한다() {
        // given
        when(productDao.findAll()).thenReturn(Arrays.asList(식당_포스.삼겹살, 식당_포스.목살, 식당_포스.김치찌개, 식당_포스.공깃밥));

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).containsExactly(식당_포스.삼겹살, 식당_포스.목살, 식당_포스.김치찌개, 식당_포스.공깃밥);
    }
}
