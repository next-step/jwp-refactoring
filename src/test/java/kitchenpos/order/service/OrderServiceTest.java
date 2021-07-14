package kitchenpos.order.service;

import static kitchenpos.menu.domain.MenuProductTest.*;
import static kitchenpos.menu.domain.MenuTest.*;
import static kitchenpos.order.domain.OrderLineItemDetailTest.*;
import static kitchenpos.order.domain.OrderStatus.*;
import static kitchenpos.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemDetails;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableTest;
import kitchenpos.order.domain.OrderTest;
import kitchenpos.generic.quantity.domain.Quantity;
import kitchenpos.order.dto.OrderLineItemDetailRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.service.OrderService;
import kitchenpos.order.domain.OrderValidator;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스")
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderValidator orderValidator;
    @Mock
    OrderRepository orderRepository;

    OrderTable 테이블100_사용중;
    
    OrderLineItemDetailRequest 후라이드_한마리_상세_요청;
    OrderLineItemDetailRequest 양념치킨_한마리_상세_요청;

    OrderLineItemRequest 후라이드_한마리_요청;
    OrderLineItemRequest 양념치킨_한마리_요청;
    OrderRequest 양념_후라이드_각_한마리_요청;

    OrderLineItem 후라이드_한마리;
    OrderLineItem 양념치킨_한마리;
    Order 양념_후라이드_각_한마리;

    @BeforeEach
    void setUp() {
        테이블100_사용중 = OrderTableTest.orderTable(100L, NumberOfGuests.of(4), false);

        후라이드_한마리_상세_요청 = new OrderLineItemDetailRequest(MP1후라이드.getSeq(), 후라이드.getId(), "후라이드", BigDecimal.valueOf(16000), 1);
        양념치킨_한마리_상세_요청 = new OrderLineItemDetailRequest(MP2양념치킨.getSeq(), 양념치킨.getId(), "양념치킨", BigDecimal.valueOf(16000), 1);

        후라이드_한마리_요청 = new OrderLineItemRequest(후라이드_메뉴.getId(), 후라이드_메뉴.getName(), 후라이드_메뉴.getPrice().value(),
            1, Arrays.asList(후라이드_한마리_상세_요청));
        양념치킨_한마리_요청 = new OrderLineItemRequest(양념치킨_메뉴.getId(), 양념치킨_메뉴.getName(), 양념치킨_메뉴.getPrice().value(),
            1, Arrays.asList(양념치킨_한마리_상세_요청));
        양념_후라이드_각_한마리_요청 = new OrderRequest(테이블100_사용중.getId(), Arrays.asList(후라이드_한마리_요청, 양념치킨_한마리_요청));

        후라이드_한마리 = new OrderLineItem(후라이드_메뉴.getId(), 후라이드_메뉴.getName(), 후라이드_메뉴.getPrice(),
            Quantity.valueOf(1), OrderLineItemDetails.of(후라이드_주문내역_상세));
        양념치킨_한마리 = new OrderLineItem(양념치킨_메뉴.getId(), 양념치킨_메뉴.getName(), 양념치킨_메뉴.getPrice(),
            Quantity.valueOf(1), OrderLineItemDetails.of(양념치킨_주문내역_상세));
        양념_후라이드_각_한마리 = OrderTest.order(100L, 테이블100_사용중.getId(), COOKING, OrderLineItems.of(후라이드_한마리, 양념치킨_한마리));
    }

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        when(orderRepository.save(any())).thenReturn(양념_후라이드_각_한마리);

        // when
        Order savedOrder = orderService.create(양념_후라이드_각_한마리_요청);

        // then
        assertThat(savedOrder.getOrderTableId()).isEqualTo(양념_후라이드_각_한마리_요청.getOrderTableId());
        assertThat(savedOrder.getOrderLineItems().contains(후라이드_한마리)).isTrue();
        assertThat(savedOrder.getOrderLineItems().contains(양념치킨_한마리)).isTrue();
    }

    @Test
    @DisplayName("주문 목록을 가져온다")
    void list() {
        // given
        Order 양념_후라이드_추가 = OrderTest.order(1L, 테이블100_사용중.getId(), COOKING, OrderLineItems.of(후라이드_한마리, 양념치킨_한마리));
        when(orderRepository.findAll()).thenReturn(Arrays.asList(양념_후라이드_각_한마리, 양념_후라이드_추가));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders).containsExactly(양념_후라이드_각_한마리, 양념_후라이드_추가);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        Order 조리중인_주문 = 양념_후라이드_각_한마리;
        OrderRequest 식사중_상태 = new OrderRequest(OrderStatus.MEAL);
        when(orderRepository.findById(any())).thenReturn(Optional.of(조리중인_주문));

        // when
        Order changedOrder = orderService.changeOrderStatus(조리중인_주문.getId(), 식사중_상태);

        // then
        assertThat(changedOrder.getId()).isEqualTo(조리중인_주문.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(식사중_상태.getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태 변경 실패(주문이 존재하지 않음)")
    void changeOrderStatus_failed1() {
        // given
        Order 조리중인_주문 = 양념_후라이드_각_한마리;
        OrderRequest 식사중_상태 = new OrderRequest(OrderStatus.MEAL);
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(조리중인_주문.getId(), 식사중_상태))
            .isInstanceOf(OrderNotFoundException.class);
    }
}
