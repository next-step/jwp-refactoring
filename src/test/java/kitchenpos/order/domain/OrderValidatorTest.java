package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @BeforeEach
    void setUp() {
        Menu menu = new Menu();
    }

    @DisplayName("유효햔 메뉴")
    @Test
    void validateMenu() {
        // given
        ID로_메뉴_조회(new Menu());
        메뉴_개수_반환(1);

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));

        // when and then
        assertThatCode(() -> orderValidator.validateMenu(orderLineItems))
            .doesNotThrowAnyException();
    }

    @DisplayName("메뉴가 없을 때 에러")
    @Test
    void validateMenuErrorWhenMenuIsNull() {
        // given
        ID로_메뉴_조회(null);
        // 메뉴_개수_반환(1);

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> orderValidator.validateMenu(orderLineItems));
    }

    @DisplayName("메뉴의 개수가 요청으로 들어온 개수와 다르면 에러")
    @Test
    void validateMenuErrorWhenInvalidMenuSize() {
        // given
        ID로_메뉴_조회(new Menu());
        메뉴_개수_반환(2);

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1);
        OrderLineItems orderLineItems = new OrderLineItems(Collections.singletonList(orderLineItem));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderValidator.validateMenu(orderLineItems))
            .withMessage("주문 항목의 개수가 다릅니다.");
    }

    private void ID로_메뉴_조회(Menu menu) {
        Mockito.when(menuRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.ofNullable(menu));
    }

    private void 메뉴_개수_반환(long count) {
        Mockito.when(menuRepository.countByIdIn(Mockito.anyList()))
            .thenReturn(count);
    }
}
