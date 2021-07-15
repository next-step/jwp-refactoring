package kitchenpos.event;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.event.OrderCreatedEvent;
import kitchenpos.orderlineitem.domain.OrderLineItemRepository;
import kitchenpos.orderlineitem.event.OrderLineItemEventHandler;
import kitchenpos.orderlineitem.exception.EmptyOrderLineItemsException;
import kitchenpos.orderlineitem.exception.MenuAndOrderLineItemSizeNotMatchException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderLineItemEventTest {
    @Mock
    OrderLineItemRepository orderLineItemRepository;
    @Mock
    MenuRepository menuRepository;

    Order savedOrder;

    OrderLineItemEventHandler orderLineItemEventHandler;
    OrderCreatedEvent orderCreatedEvent;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 4, false);
        savedOrder = new Order(1L, orderTable, OrderStatus.COOKING);

        orderCreatedEvent = new OrderCreatedEvent(savedOrder, Arrays.asList(
                new OrderLineItemRequest(1L, 1L),
                new OrderLineItemRequest(2L, 1L)
        ));

        orderLineItemEventHandler = new OrderLineItemEventHandler(orderLineItemRepository, menuRepository);
    }

    @DisplayName("주문 항목이 아무것도 입력이 안되었을 경우")
    @Test
    void 주문_항목이_아무것도_입력이_안되었을_경우() {
        //given
        orderCreatedEvent = new OrderCreatedEvent(savedOrder, Collections.emptyList());

        //when, then
        assertThatThrownBy(() -> orderLineItemEventHandler.saveOrderLineItem(orderCreatedEvent))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @DisplayName("주문 항목의 메뉴 중 등록되지 않은 메뉴가 있을 때")
    @Test
    void 주문_항목의_메뉴_중_등록되지_않은_메뉴가_있을_때() {
        //given
        given(menuRepository.countByIdIn(anyList())).willReturn(1L);

        //when, then
        assertThatThrownBy(() -> orderLineItemEventHandler.saveOrderLineItem(orderCreatedEvent))
                .isInstanceOf(MenuAndOrderLineItemSizeNotMatchException.class);
    }
}
