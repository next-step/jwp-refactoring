package kitchenpos.product.service;

import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.menu.MenuFactory;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() {
        // given
        final String name = "치킨";
        final int price = 3500;
        ProductRequest 치킨_상품 = MenuFactory.ofProductRequest(name, price);
        ProductResponse expected = MenuFactory.ofProductResponse(1L, name, price);
        given(productRepository.save(치킨_상품.toProduct())).willReturn(MenuFactory.ofProduct(1L, name, price));

        // when
        ProductResponse response = productService.create(치킨_상품);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(expected.getId()),
                () -> assertThat(response.getName()).isEqualTo(치킨_상품.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(치킨_상품.getPrice())
        );
    }

    @DisplayName("상품 생성 시 가격이 0원보다 낮으면 안된다.")
    @Test
    void 상품_생성_가격_0원_미만_예외() {
        // given
        final String name = "치킨";
        final int price = -1;
        ProductRequest 치킨_상품 = MenuFactory.ofProductRequest(name, price);

        // when
        Throwable thrown = catchThrowable(() -> productService.create(치킨_상품));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록_조회() {
        // given
        Product 치킨_예상결과 = MenuFactory.ofProduct(1L, "치킨", 3500);
        Product 콜라_예상결과 = MenuFactory.ofProduct(2L, "콜라", 500);
        given(productRepository.findAll()).willReturn(Arrays.asList(치킨_예상결과, 콜라_예상결과));

        // when
        List<ProductResponse> response = productService.list();

        // then
        assertThat(response).containsExactly(ProductResponse.of(치킨_예상결과), ProductResponse.of(콜라_예상결과));
    }
}
