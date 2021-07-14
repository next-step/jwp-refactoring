package kitchenpos.product.application;

import static kitchenpos.exception.KitchenposExceptionMessage.MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.Price;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductValidateImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductValidateImpl productValidate;


    @DisplayName("메뉴 가격이 주어진 상품보다 낮은지 체크 테스트")
    @Test
    void checkNotOverPriceTest() {
        // given
        Mockito.when(productRepository.findById(1L))
               .thenReturn(
                   Optional.of(new Product("테스트 상품1", Price.of(BigDecimal.valueOf(1000L)))));
        Mockito.when(productRepository.findById(2L))
               .thenReturn(
                   Optional.of(new Product("테스트 상품2", Price.of(BigDecimal.valueOf(2000L)))));

        // when
        assertThatCode(() -> productValidate.checkOverPrice(Price.of(BigDecimal.valueOf(10000L)),
                                                            Arrays.asList(
                                                                new MenuProductRequest(1L, 3),
                                                                new MenuProductRequest(2L, 4))))
            .doesNotThrowAnyException();
    }

    @DisplayName("메뉴 가격이 주어진 상품보다 높으면 에러")
    @Test
    void checkOverPriceTest() {
        // given
        Mockito.when(productRepository.findById(1L))
               .thenReturn(
                   Optional.of(new Product("테스트 상품1", Price.of(BigDecimal.valueOf(1000L)))));
        Mockito.when(productRepository.findById(2L))
               .thenReturn(
                   Optional.of(new Product("테스트 상품2", Price.of(BigDecimal.valueOf(2000L)))));

        // when
        assertThatThrownBy(
            () -> productValidate.checkOverPrice(Price.of(BigDecimal.valueOf(30000L)),
                                                 Arrays.asList(new MenuProductRequest(1L, 3),
                                                               new MenuProductRequest(2L, 4))))
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE.getMessage());
    }


}
