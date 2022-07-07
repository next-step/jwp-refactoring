package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void 상품_생성() {
        // given
        ProductRequest request = ProductRequest.of("떡볶이", new BigDecimal(1000));
        given(productRepository.save(any(Product.class))).willReturn(Product.of(1L, "떡볶이", new BigDecimal(1000)));

        // when
        ProductResponse response = productService.create(request);

        // then
        assertAll(() -> assertThat(response).isNotNull(), () -> assertThat(response.getId()).isNotNull());
    }

    @DisplayName("상품 가격이 0보자 작으면 생성할 수 없다.")
    @Test
    void 상품_가격이_0보다_작으면_생성_실패() {
        // given
        ProductRequest request = ProductRequest.of("떡볶이", new BigDecimal(-1));

        // when / then
        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 없는 상품은 생성할 수 없다.")
    @Test
    void 가격이_없는_상품_생성_실패() {
        // given
        ProductRequest request = ProductRequest.of("떡볶이", null);

        // when / then
        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 상품을 조회할 수 있다.")
    @Test
    void 전체_상품_조회() {
        // given
        Product 떡볶이 = Product.of("떡볶이", 1000);
        Product 순대 = Product.of("순대", 1500);
        given(productRepository.findAll()).willReturn(Arrays.asList(떡볶이, 순대));

        // when
        List<ProductResponse> response = productService.list();

        // then
        assertThat(response).hasSize(2);
    }
}
