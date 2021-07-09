package kitchenpos.product.application;

import kitchenpos.exception.KitchenposException;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static kitchenpos.exception.KitchenposExceptionMessage.MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE;
import static kitchenpos.exception.KitchenposExceptionMessage.PRICE_CANNOT_LOWER_THAN_MIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성 테스트")
    @Test
    void createTest() {
        // given
        ProductRequest productRequest = new ProductRequest("불고기", BigDecimal.valueOf(1000L));
        Product 불고기 = new Product("불고기", BigDecimal.valueOf(1000L));
        Mockito.when(productRepository.save(any())).thenReturn(불고기);

        // when
        ProductResponse actual = productService.create(productRequest);

        // then
        assertThat(actual).isNotNull()
                          .extracting(product -> product.getName())
                          .isEqualTo(불고기.getName());
    }

    @DisplayName("가격이 0 이하인 상품 등록 테스트")
    @Test
    void createTestWithWrongPrice() {
        // given
        ProductRequest productRequest = new ProductRequest("불고기", BigDecimal.valueOf(-1L));

        // when
        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE.getMessage());
    }
}
