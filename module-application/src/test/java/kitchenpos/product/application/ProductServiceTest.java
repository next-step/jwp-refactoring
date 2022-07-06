package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.product.ProductService;
import kitchenpos.common.common.domain.Price;
import kitchenpos.common.product.domain.Product;
import kitchenpos.common.product.dto.ProductRequest;
import kitchenpos.common.product.dto.ProductResponse;
import kitchenpos.common.product.repository.ProductRepository;
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
        final ProductResponse actual = productService.create(given);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void 상품_목록을_조회할_수_있어야_한다() {
        // given
        when(productRepository.findAll()).thenReturn(Arrays.asList(식당_포스.삼겹살, 식당_포스.목살, 식당_포스.김치찌개, 식당_포스.공깃밥));

        // when
        final List<ProductResponse> actual = productService.list();

        // then
        assertThat(actual.stream().map(ProductResponse::getId).collect(Collectors.toList()))
                .containsExactly(식당_포스.삼겹살.getId(), 식당_포스.목살.getId(), 식당_포스.김치찌개.getId(), 식당_포스.공깃밥.getId());
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
