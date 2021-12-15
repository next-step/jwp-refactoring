package kitchenpos.order.domain;

import kitchenpos.menu.domain.menu.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.order.Order;
import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.order.domain.orderLineItem.OrderLineItemRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.sns.application.SnsComponent;
import kitchenpos.table.domain.table.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.MenuDomainFixture.후라이드_치킨;
import static kitchenpos.fixture.OrderDomainFixture.첫_주문;
import static kitchenpos.fixture.OrderTableDomainFixture.한식_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Mockito - 주문 관리")
class OrderMockitoTest {

    private MenuRepository menuRepository;
    private OrderRepository orderRepository;
    private OrderLineItemRepository orderLineItemRepository;
    private OrderTableRepository orderTableRepository;
    private OrderService orderService;
    private SnsComponent snsComponent;

    private void setUpMock() {
        this.menuRepository = mock(MenuRepository.class);
        this.orderRepository = mock(OrderRepository.class);
        this.orderLineItemRepository = mock(OrderLineItemRepository.class);
        this.orderTableRepository = mock(OrderTableRepository.class);
        this.snsComponent = mock(SnsComponent.class);
        this.orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository,
                orderTableRepository, snsComponent);
    }

    @Test
    @DisplayName("주문 내역 조회")
    void findAllOrder() {
        // given
        setUpMock();
        when(orderRepository.findAll()).thenReturn(Lists.newArrayList(첫_주문));

        // when
        final List<OrderResponse> actual = orderService.findAllOrder();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("주문 생성")
    void create() {
        // given
        setUpMock();
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(한식_테이블));
        when(orderRepository.save(any(Order.class))).thenReturn(첫_주문);
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(후라이드_치킨));
        OrderLineItemRequest 주문_내역 = OrderLineItemRequest.of(anyLong(), 2);
        OrderRequest 첫_주문_요청 = OrderRequest.of(1L, Lists.newArrayList(주문_내역));

        // when
        final OrderResponse actual = orderService.saveOrder(첫_주문_요청);

        // then
        assertThat(actual).isNotNull();
    }

}
