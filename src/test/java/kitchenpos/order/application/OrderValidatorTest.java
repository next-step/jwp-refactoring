package kitchenpos.order.application;

import kitchenpos.menu.application.exception.MenuNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.application.exception.TableNotFoundException;
import kitchenpos.order.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("주문 조건 테스트")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private TableRepository tableRepository;
    @InjectMocks
    private OrderValidator orderValidator;

    private Menu 매콤치킨단품;
    private OrderTable 빈테이블;
    private OrderLineItem 주문항목;
    private Order 주문;

    @BeforeEach
    void setUp() {
        MenuProduct 매콤치킨구성 = new MenuProduct(1L, 1L);
        매콤치킨단품 = Menu.of("매콤치킨단품", BigDecimal.valueOf(13000), 1L, Collections.singletonList(매콤치킨구성));

        빈테이블 = OrderTable.of(0, new TableState(true));
        주문항목 = OrderLineItem.of(1L, 1L);
        주문 = Order.of(1L, COOKING, Collections.singletonList(주문항목));
    }

    @Test
    @DisplayName("주문 항목의 목록이 비어있는 경우 예외가 발생한다.")
    void validateOrderLineItemsEmpty() {
        Order 주문 = Order.of(1L, COOKING, Collections.emptyList());

        assertThatThrownBy(() -> orderValidator.validate(주문))
                .isInstanceOf(TableNotFoundException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴가 기존에 등록된 메뉴가 아닌 경우 예외가 발생한다.")
    void validateOrderLineItems() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderValidator.validate(주문))
                .isInstanceOf(MenuNotFoundException.class);
        verify(menuRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(매콤치킨단품));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(빈테이블));

        assertThatThrownBy(() -> orderValidator.validate(주문))
                .isInstanceOf(InvalidTableState.class);
        verify(menuRepository, times(1)).findById(anyLong());
        verify(tableRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("주문 상태가 완료인 경우 예외가 발생한다.")
    void validateOrderStatus() {
        assertThatThrownBy(() -> orderValidator.validateOrderStatus(COMPLETION))
                .isInstanceOf(InvalidOrderState.class);
    }
}
