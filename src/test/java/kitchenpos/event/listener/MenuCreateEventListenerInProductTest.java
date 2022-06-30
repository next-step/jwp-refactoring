package kitchenpos.event.listener;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import kitchenpos.domain.Product;
import kitchenpos.dto.event.MenuCreatedEvent;
import kitchenpos.event.customEvent.MenuCreateEvent;
import kitchenpos.exception.MenuException;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuCreateEventListenerInProductTest {

    private MenuCreateEventListenerInProduct eventListener;

    @Mock
    private ProductRepository productRepository;
    private Product pizza;
    private Product chicken;

    @BeforeEach
    public void init() {
        eventListener = new MenuCreateEventListenerInProduct(productRepository);

        chicken = new Product("chicken", BigDecimal.valueOf(500));
        pizza = new Product("pizza", BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("Menu에 포함된 Product의 합의 총합은 Menu Price보다 커야한다.")
    public void validatePriceSmallThenSumTest() {
        //given
        Map<Long, Long> quantityPerProduct = new HashMap<>();
        quantityPerProduct.put(1L, 1L);
        quantityPerProduct.put(2L, 1L);
        MenuCreatedEvent eventDTO = new MenuCreatedEvent(quantityPerProduct,
            BigDecimal.valueOf(3000));
        MenuCreateEvent event = new MenuCreateEvent(eventDTO);

        when(productRepository.findById(1L)).thenReturn(
            Optional.of(chicken));
        when(productRepository.findById(2L)).thenReturn(
            Optional.of(pizza));

        //when & then
        assertThatThrownBy(() -> eventListener.onApplicationEvent(event))
            .isInstanceOf(MenuException.class)
            .hasMessage("메뉴 가격은 상품 가격 총합보다 작아야합니다");
    }


    @Test
    @DisplayName("저장된 상품이 아니면 에러를 발생한다.")
    public void validateSavedProduct() {
        //given
        Map<Long, Long> quantityPerProduct = new HashMap<>();
        quantityPerProduct.put(1L, 1L);
        quantityPerProduct.put(2L, 1L);
        MenuCreatedEvent eventDTO = new MenuCreatedEvent(quantityPerProduct,
            BigDecimal.valueOf(3000));
        MenuCreateEvent event = new MenuCreateEvent(eventDTO);

        when(productRepository.findById(1L)).thenReturn(
            Optional.of(chicken));

        //when & then
        assertThatThrownBy(() -> eventListener.onApplicationEvent(event))
            .isInstanceOf(MenuException.class)
            .hasMessage("상품이 저장되어있지 않습니다");
    }

}