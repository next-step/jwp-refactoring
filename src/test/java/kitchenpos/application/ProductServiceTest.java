package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.fixture.ProductFixture;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 상품_후라이드;
    private Product 상품_치킨무;
    private Product 상품_양념소스;

    @BeforeEach
    void setUp() {
        상품_후라이드 = ProductFixture.create(1L, "후라이드", 15_000L);
        상품_치킨무 = ProductFixture.create(2L, "치킨무", 500L);
        상품_양념소스 = ProductFixture.create(3L, "양념소스", 500L);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        given(productRepository.save(상품_후라이드)).willReturn(상품_후라이드);

        Product savedProduct = productService.create(상품_후라이드);

        assertThat(savedProduct).isEqualTo(상품_후라이드);
    }

    @DisplayName("상품을 등록할 때 가격이 null이면 예외가 발생한다.")
    @Test
    void createInvalidPrice1() {
        Product nullPriceProduct = 상품_후라이드;
        nullPriceProduct.setPrice(null);

        assertThatThrownBy(() -> productService.create(nullPriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 등록할 때 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void createInvalidPrice2() {
        Product wrongPriceProduct = ProductFixture.create(1L, "생닭", -1000L);

        assertThatThrownBy(() -> productService.create(wrongPriceProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        given(productRepository.findAll()).willReturn(Arrays.asList(상품_후라이드, 상품_치킨무, 상품_양념소스));

        List<Product> products = productService.list();

        assertThat(products).containsExactly(상품_후라이드, 상품_치킨무, 상품_양념소스);
    }
}