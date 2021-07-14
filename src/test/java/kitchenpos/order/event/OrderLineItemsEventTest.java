package kitchenpos.order.event;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.event.order.OrderCreatedEvent;
import kitchenpos.order.event.order.OrderLineItemsEventHandler;
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
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
public class OrderLineItemsEventTest {
    @Mock
    OrderLineItemRepository orderLineItemRepository;

    @Mock
    MenuRepository menuRepository;

    private Order savedOrder;
    private TableGroup tableGroup;
    private OrderTable orderTable;
    private OrderLineItemsEventHandler orderLineItemEventHandler;
    private OrderCreatedEvent orderCreatedEvent;

    @BeforeEach
    void setUp() {
        테이블_그룹_주문_테이블_생성();
        주문_생성();
        주문_이벤트_생성();
        orderLineItemEventHandler = new OrderLineItemsEventHandler(menuRepository, orderLineItemRepository);
    }

    private void 테이블_그룹_주문_테이블_생성() {
        tableGroup = new TableGroup(1L);
        orderTable = new OrderTable(1L, tableGroup, 4, false);
    }

    private void 주문_생성() {
        savedOrder = new Order(1L, orderTable, OrderStatus.COOKING);
    }

    private void 주문_이벤트_생성() {
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, savedOrder, 1L, 1L);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, savedOrder, 1L, 1L);
        orderCreatedEvent = new OrderCreatedEvent(savedOrder, Arrays.asList(orderLineItem1, orderLineItem2));

    }

    @DisplayName("주문에는 메뉴가 1개 이상 필요하다.")
    @Test
    void createOrderException1() {
        orderCreatedEvent = new OrderCreatedEvent(savedOrder, Collections.emptyList());

        주문_메뉴_0개일_경우_예외_발생함();
    }

    private void 주문_메뉴_0개일_경우_예외_발생함() {
        assertThatThrownBy(() -> orderLineItemEventHandler.savedOrderLineItems(orderCreatedEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문에는 메뉴가 1개 이상 필요합니다.");

    }

    @DisplayName("메뉴에 등록되지 않은 주문 항목은 주문 할 수 없다.")
    @Test
    void createOrderException2(){
        given(menuRepository.findAllById(anyList())).willReturn(Arrays.asList());

        미등록_메뉴_주문시_예외_발생함();
    }

    private void 미등록_메뉴_주문시_예외_발생함() {
        assertThatThrownBy(() -> orderLineItemEventHandler.savedOrderLineItems(orderCreatedEvent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 메뉴만 주문할 수 있습니다.");
    }
}
