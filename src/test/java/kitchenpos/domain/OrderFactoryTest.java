package kitchenpos.domain;

import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderFactoryTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderFactory orderFactory;

    @DisplayName("주문을 생성한다")
    @Test
    void testCreate() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);

        Menu 커플세트 = new Menu(1L, "커플세트", 16000, new MenuGroup(), Collections.emptyList());
        Menu 혼밥세트 = new Menu(2L, "혼밥세트", 16000, new MenuGroup(), Collections.emptyList());
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(커플세트.getId(), 1);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(혼밥세트.getId(), 1);
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(anyLong())).willReturn(Optional.of(커플세트), Optional.of(혼밥세트));

        // when
        Order order = orderFactory.create(orderRequest);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems()).map(OrderLineItem::getMenu)
                .contains(커플세트, 혼밥세트);
    }

    @DisplayName("주문 항목이 있어야 한다")
    @Test
    void requiredOrderItem() {
        // given
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderFactory.create(orderRequest);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목에 메뉴에 있는 메뉴만 있어야 한다")
    @Test
    void validateMenu() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);

        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderFactory.create(orderRequest);

        // then
        assertThatThrownBy(callable).isInstanceOf(MenuNotFoundException.class);
    }

    @DisplayName("주문 테이블이 있어야 한다")
    @Test
    void validateTable() {
        OrderTable orderTable = new OrderTable(1L, null, 4, false);

        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderFactory.create(orderRequest);

        // then
        assertThatThrownBy(callable).isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("주문 테이블이 비어있지 않아야 한다")
    @Test
    void notEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);

        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderFactory.create(orderRequest);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
