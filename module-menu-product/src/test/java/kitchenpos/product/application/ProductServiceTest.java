package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.fixture.ProductFactory;
import kitchenpos.product.domain.Product;

import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductResponse;
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
    private Product 토마토;
    private Product 감자;

    @BeforeEach
    void setUp() {
        토마토 = ProductFactory.createProduct(1L, "토마토", 1000);
        감자 = ProductFactory.createProduct(2L, "감자", 2000);
    }

    @Test
    void 상품_생성() {
        // given
        given(productRepository.save(any(Product.class))).willReturn(토마토);

        // when
        ProductResponse result = productService.create(
                ProductFactory.createProductRequest(토마토.getName().getValue(), 토마토.getPrice().getValue())
        );

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(토마토.getId()),
                () -> assertThat(result.getName()).isEqualTo(토마토.getName().getValue()),
                () -> assertThat(result.getPrice()).isEqualTo(토마토.getPrice().getValue())
        );
    }

    @Test
    void 상품_생성_가격_없는_경우_예외() {
        assertThatThrownBy(
                () -> productService.create(ProductFactory.createProductRequest("공기", null))
        ).isInstanceOf(InvalidPriceException.class);
    }

    @Test
    void 상품_생성_가격_0_미만_예외() {
        assertThatThrownBy(
                () -> productService.create(ProductFactory.createProductRequest("공기", BigDecimal.valueOf(-1)))
        ).isInstanceOf(InvalidPriceException.class);
    }

    @Test
    void 상품_목록_조회() {
        // given
        given(productRepository.findAll()).willReturn(Arrays.asList(토마토, 감자));

        List<ProductResponse> result = productService.list();
        assertThat(toIdList(result)).containsExactlyElementsOf(Arrays.asList(토마토.getId(), 감자.getId()));
    }

    private List<Long> toIdList(List<ProductResponse> productResponses) {
        return productResponses.stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());
    }
}
