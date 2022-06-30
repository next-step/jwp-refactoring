package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.fixture.UnitTestFixture;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

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
        final ProductRequest given = new ProductRequest("new product", new Price(15000L));

        final Product expected = new Product(1L, "new product", new Price(15000L));
        when(productRepository.save(any(Product.class))).thenReturn(expected);

        // when
        final Product actual = productService.create(given);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 상품_목록을_조회할_수_있어야_한다() {
        // given
        when(productRepository.findAll()).thenReturn(Arrays.asList(식당_포스.삼겹살, 식당_포스.목살, 식당_포스.김치찌개, 식당_포스.공깃밥));

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).containsExactly(식당_포스.삼겹살, 식당_포스.목살, 식당_포스.김치찌개, 식당_포스.공깃밥);
    }

    @Test
    void 아이디로_상품을_조회할_수_있어야_한다() {
        // given
        final Product given = new Product(1L, "new product", new Price(15000L));
        when(productRepository.findById(given.getId())).thenReturn(Optional.of(given));

        // when
        final Product actual = productService.getById(given.getId());

        // then
        assertThat(actual).isEqualTo(given);
    }
}
