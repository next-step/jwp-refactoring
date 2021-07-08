package kitchenpos.application;

import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderTableTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 서비스")
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    MenuService menuService;
    @Mock
    TableService tableService;
    @Mock
    OrderRepository orderRepository;

    OrderTable 테이블100_사용중 = new OrderTable(100L, 4, false);

    OrderLineItemRequest 후라이드_한마리_요청;
    OrderLineItemRequest 양념치킨_한마리_요청;
    OrderRequest 양념_후라이드_각_한마리_요청;

    OrderLineItem 후라이드_한마리;
    OrderLineItem 양념치킨_한마리;
    Order 양념_후라이드_각_한마리;

    @BeforeEach
    void setUp() {
        후라이드_한마리_요청 = new OrderLineItemRequest(후라이드_메뉴.getId(), 1);
        양념치킨_한마리_요청 = new OrderLineItemRequest(양념치킨_메뉴.getId(), 1);
        양념_후라이드_각_한마리_요청 = new OrderRequest(테이블100_사용중.getId(), Arrays.asList(후라이드_한마리_요청, 양념치킨_한마리_요청));

        후라이드_한마리 = new OrderLineItem(후라이드_메뉴, 1);
        양념치킨_한마리 = new OrderLineItem(양념치킨_메뉴, 1);
        양념_후라이드_각_한마리
            = new Order(100L, COOKING, Arrays.asList(후라이드_한마리, 양념치킨_한마리));
        테이블100_사용중.addOrder(양념_후라이드_각_한마리);
    }

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        when(tableService.findById(테이블100_사용중.getId())).thenReturn(테이블100_사용중);

        // when
        Order savedOrder = orderService.create(양념_후라이드_각_한마리_요청);

        // then
        assertThat(savedOrder.getOrderTable().getId()).isEqualTo(테이블100_사용중.getId());
        assertThat(savedOrder.getOrderLineItems().contains(후라이드_한마리)).isTrue();
        assertThat(savedOrder.getOrderLineItems().contains(양념치킨_한마리)).isTrue();
    }

    @Test
    @DisplayName("주문 생성 실패(주문 항목 비어있음)")
    void create_failed1() {
        // given
        OrderRequest 주문내역이_없는_주문 = new OrderRequest(테이블100_사용중.getId(), Collections.emptyList());
        when(tableService.findById(테이블100_사용중.getId())).thenReturn(테이블100_사용중);

        // then
        assertThatThrownBy(() -> orderService.create(주문내역이_없는_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(메뉴가 존재하지 않음")
    void create_failed2() {
        // given
        when(menuService.findById(any())).thenThrow(OrderTableNotFoundException.class);

        // then
        assertThatThrownBy(() -> orderService.create(양념_후라이드_각_한마리_요청))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(테이블 없음)")
    void create_failed3() {
        // given
        when(tableService.findById(any())).thenThrow(OrderTableNotFoundException.class);

        // then
        assertThatThrownBy(() -> orderService.create(양념_후라이드_각_한마리_요청))
            .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("주문 생성 실패(테이블이 empty)")
    void create_failed4() {
        // given
        when(tableService.findById(any())).thenReturn(테이블3);

        // then
        assertThatThrownBy(() -> orderService.create(양념_후라이드_각_한마리_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 가져온다")
    void list() {
        // given
        Order 양념_후라이드_추가 = new Order(1L, COOKING, Arrays.asList(후라이드_한마리, 양념치킨_한마리));
        테이블100_사용중.addOrder(양념_후라이드_추가);
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

    @Test
    @DisplayName("주문 상태 변경 실패(계산완료 상태)")
    void changeOrderStatus_failed2() {
        // given
        Order 계산완료_주문 = new Order(1L, COMPLETION, Arrays.asList(후라이드_한마리, 양념치킨_한마리));
        OrderRequest 식사중인_주문 = new OrderRequest(OrderStatus.MEAL);
        테이블100_사용중.addOrder(계산완료_주문);
        when(orderRepository.findById(any())).thenReturn(Optional.of(계산완료_주문));

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(계산완료_주문.getId(), 식사중인_주문))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
