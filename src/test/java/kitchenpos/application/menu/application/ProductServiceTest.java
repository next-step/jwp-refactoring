package kitchenpos.application.menu.application;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 아메리카노;
    private Product 바닐라라떼;

    @BeforeEach
    void set_up() {
        아메리카노 = new Product("아메리카노", BigDecimal.valueOf(5_000));
        바닐라라떼 = new Product("바닐라라떼", BigDecimal.valueOf(8_000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        when(productRepository.save(any())).thenReturn(아메리카노);

        // when
        ProductResponse 상품_등록 = productService.create(
            new ProductRequest("강정치킨", BigDecimal.valueOf(5_000)));

        // then
        assertAll(
            () -> assertThat(상품_등록.getName()).isEqualTo(아메리카노.getName()),
            () -> assertThat(상품_등록.getPrice()).isEqualTo(아메리카노.getPrice().getPrice())
        );
    }

    @DisplayName("상품 가격이 올바르지 않으면 예외가 발생한다.")
    @Test
    void createNullPriceProductionException() {
        // given && when && then
        assertThatThrownBy(() -> new Product("name", BigDecimal.valueOf(-100_000)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 0 보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -20000})
    void createUnderZeroPriceProductionException(int input) {
        // given & when & then
        assertThatThrownBy(
            () -> productService.create(new ProductRequest("아메리카노", BigDecimal.valueOf(input))))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void findAllProduct() {
        // given
        when(productRepository.findAll()).thenReturn(Arrays.asList(아메리카노, 바닐라라떼));

        // when
        List<ProductResponse> 상품목록 = productService.list();

        // then
        assertAll(
            () -> assertThat(상품목록).hasSize(2),
            () -> assertThat(상품목록.get(0).getName()).isEqualTo(아메리카노.getName()),
            () -> assertThat(상품목록.get(0).getPrice()).isEqualTo(아메리카노.getPrice().getPrice()),
            () -> assertThat(상품목록.get(1).getName()).isEqualTo(바닐라라떼.getName()),
            () -> assertThat(상품목록.get(1).getPrice()).isEqualTo(바닐라라떼.getPrice().getPrice())
        );
    }
}
