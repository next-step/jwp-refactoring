package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.KitchenposException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 생성")
    @Test
    void create() {
        // given
        Mockito.when(menuDao.countByIdIn(Mockito.anyList()))
            .thenReturn(1L);

        OrderTable orderTable = new OrderTable(1L, new TableGroup(1L), 4, false);
        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));

        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 1);

        Order order = new Order(1L, new OrderTable(1L), OrderStatus.COOKING, LocalDateTime.now(),
            new OrderLineItems(Collections.singletonList(orderLineItem)));
        Mockito.when(orderDao.save(Mockito.any()))
            .thenReturn(order);

        OrderLineItemRequest requestOrderLineItem = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Collections.singletonList(requestOrderLineItem));

        // when
        OrderResponse actual = orderService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(actual.getOrderTableId()).isEqualTo(1L),
            () -> assertThat(actual.getOrderLineItems()).isEqualTo(Collections.singletonList(
                OrderLineItemResponse.from(orderLineItem.toEntity())))
        );
    }

    @DisplayName("주문 항목 없을 시 주문 불가능")
    @Test
    void createOrderFailWhenNoItem() {
        // given
        OrderRequest request = new OrderRequest(1L, Collections.emptyList());

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.create(request))
        .withMessage("주문 항목이 비어있습니다.");
    }

    @DisplayName("주문 항목과 메뉴 숫자가 다른 경우(메뉴에 없는 주문 항목) 주문 불가능")
    @Test
    void createOrderFailWhenItemNotExists() {
        // given
        Mockito.when(menuDao.countByIdIn(Mockito.anyList()))
            .thenReturn(2L);

        OrderLineItemRequest requestOrderLineItem = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Collections.singletonList(requestOrderLineItem));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.create(request))
        .withMessage("주문 항목의 개수가 다릅니다.");
    }

    @DisplayName("주문 테이블이 빈 테이블인 경우 주문 불가능")
    @Test
    void createOrderFailWhenTableNotExists() {
        // given
        Mockito.when(menuDao.countByIdIn(Mockito.anyList()))
            .thenReturn(1L);

        OrderTable orderTable = new OrderTable(1L, new TableGroup(1L), 4, true);
        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));

        OrderLineItemRequest requestOrderLineItem = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Collections.singletonList(requestOrderLineItem));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.create(request))
        .withMessage("주문 테이블이 비어있습니다.");
    }

    @DisplayName("주문 조회")
    @Test
    void list() {
        // given
        OrderLineItemRequest orderLineItem = new OrderLineItemRequest(1L, 1);

        Order order = new Order(1L, new OrderTable(1L), OrderStatus.COOKING, LocalDateTime.now(), new OrderLineItems(Collections.singletonList(orderLineItem)));
        Mockito.when(orderDao.findAll())
            .thenReturn(Collections.singletonList(order));

        // when
        List<OrderResponse> actual = orderService.list().getOrderResponses();

        // then
        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual.get(0).getId()).isEqualTo(1L),
            () -> assertThat(actual.get(0).getOrderTableId()).isEqualTo(1L),
            () -> assertThat(actual.get(0).getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(actual.get(0).getOrderLineItems()).isEqualTo(Collections.singletonList(
                OrderLineItemResponse.from(orderLineItem.toEntity())))
        );
    }

    @DisplayName("주문 상태변경")
    @Test
    void changeOrderStatus() {
        // given
        Order order = new Order(1L, new OrderTable(1L), OrderStatus.COOKING, LocalDateTime.now(), new OrderLineItems());
        Mockito.when(orderDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(order));

        OrderLineItem orderLineItem = new OrderLineItem(1L, new Order(1L), 1L, 1);
        Mockito.when(orderLineItemDao.findAllByOrder_Id(Mockito.anyLong()))
            .thenReturn(Collections.singletonList(orderLineItem));

        OrderRequest request = new OrderRequest(1L, "MEAL");

        // when
        OrderResponse actual = orderService.changeOrderStatus(1L, request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
        Mockito.verify(orderDao).save(Mockito.any());
    }

    @DisplayName("이미 완료상태이면 변경 불가능")
    @Test
    void modifyOrderStatusFailWhenAlreadyCompleted() {
        // given
        Order order = new Order(1L, new OrderTable(1L), OrderStatus.COMPLETION, LocalDateTime.now(), new OrderLineItems());
        Mockito.when(orderDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(order));

        OrderRequest request = new OrderRequest(1L, "MEAL");

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(1L, request))
        .withMessage("완료된 주문의 상태를 바꿀 수 없습니다.");
    }
}