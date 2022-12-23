package kitchenpos.product.application;

import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.product.domain.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 테스트")
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Test
    void 상품_생성() {
        // given
        ProductRequest 알리오올리오 = ProductRequest.of(상품(1L, "알리오올리오", new BigDecimal(17000)));

        given(productRepository.save(any())).willReturn(알리오올리오.toProduct());

        // when
        ProductResponse productResponse = productService.create(알리오올리오);

        // then
        verify(productRepository).save(any());
        assertThat(productResponse.getName()).isEqualTo("알리오올리오");
    }

    @DisplayName("전체 상품 목록을 조회한다")
    @Test
    void 전체_상품_목록_조회() {
        // given
        ProductRequest 알리오올리오 = ProductRequest.of(상품(1L, "알리오올리오", new BigDecimal(17000)));
        ProductRequest 쉬림프로제 = ProductRequest.of(상품(2L, "쉬림프로제", new BigDecimal(22000)));
        given(productRepository.findAll()).willReturn(Arrays.asList(알리오올리오.toProduct(), 쉬림프로제.toProduct()));

        // when
        List<ProductResponse> products = productService.list();
        List<String> productNames = products.stream().map(ProductResponse::getName).collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(productNames).containsExactly(알리오올리오.getName(), 쉬림프로제.getName())
        );
    }

    @DisplayName("가격이 음수인 상품을 생성한다")
    @Test
    void 가격이_음수인_상품_생성() {
        // given
        ProductRequest 알리오올리오 = new ProductRequest("알리오올리오", -17000);

        // when & then
        assertThatThrownBy(
                () -> productService.create(알리오올리오)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 정보가 없는 상품을 생성한다")
    @Test
    void 가격_정보가_없는_상품_생성() {
        // given
        ProductRequest 알리오올리오 = new ProductRequest("알리오올리오", 0);

        // when & then
        assertThatThrownBy(
                () -> productService.create(알리오올리오)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
