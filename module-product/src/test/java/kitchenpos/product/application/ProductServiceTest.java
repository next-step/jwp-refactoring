package kitchenpos.product.application;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionType;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest 상품_request;
    private Product 후라이드_치킨_entity;
    private Product 감자튀김_entity;

    @BeforeEach
    void setUp() {
        상품_request = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(10000L));
        후라이드_치킨_entity = Product.of("후라이드 치킨", BigDecimal.valueOf(10000L));
        감자튀김_entity = Product.of("감자튀김", BigDecimal.valueOf(5000L));
    }

    @DisplayName("상품을 등록하면 정상적으로 등록되어야 한다")
    @Test
    void create_test() {
        // given
        when(productRepository.save(any()))
                .thenReturn(후라이드_치킨_entity);

        // when
        ProductResponse result = productService.create(상품_request);

        // then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(후라이드_치킨_entity.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(후라이드_치킨_entity.getPrice())
        );
    }

    @DisplayName("상품의 이름이 없으면 예외가 발생해야 한다")
    @Test
    void create_exception_test() {
        // given
        상품_request = new ProductRequest(null, BigDecimal.valueOf(5000L));

        // then
        assertThatThrownBy(() -> {
            productService.create(상품_request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_NAME.getMessage());
    }

    @DisplayName("상품의 가격이 없으면 예외가 발생해야 한다")
    @Test
    void create_exception_test2() {
        // given
        상품_request = new ProductRequest("후라이드 치킨", null);

        // then
        assertThatThrownBy(() -> {
            productService.create(상품_request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_PRICE.getMessage());
    }

    @DisplayName("상품의 가격이 음수이면 예외가 발생해야 한다")
    @Test
    void create_exception_test3() {
        // given
        상품_request = new ProductRequest("후라이드 치킨", BigDecimal.valueOf(-3000L));

        // then
        assertThatThrownBy(() -> {
            productService.create(상품_request);
        }).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.INVALID_PRICE.getMessage());
    }

    @DisplayName("상품목록을 조회한다")
    @Test
    void findAll_test() {
        // given
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(후라이드_치킨_entity, 감자튀김_entity));

        // when
        List<ProductResponse> result = productService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
