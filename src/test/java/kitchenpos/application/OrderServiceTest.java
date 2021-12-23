package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.OrderAcceptanceTestHelper;
import kitchenpos.ui.TableAcceptanceTestHelper;

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
        OrderLineItem requestOrderLineItem = new OrderLineItem(1L, 1);

        Mockito.when(menuDao.countByIdIn(Mockito.anyList()))
            .thenReturn(1L);

        OrderTable orderTable = new OrderTable(1L, 1L, 4, false);
        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));

        Order order = new Order(1L, 1L, "COOKING", LocalDateTime.now(), new ArrayList<>());
        Mockito.when(orderDao.save(Mockito.any()))
            .thenReturn(order);

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        Mockito.when(orderLineItemDao.save(Mockito.any()))
            .thenReturn(orderLineItem);

        Order request = new Order(1L, Collections.singletonList(requestOrderLineItem));

        // when
        Order actual = orderService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(actual.getOrderTableId()).isEqualTo(1L),
            () -> assertThat(actual.getOrderLineItems()).isEqualTo(Collections.singletonList(orderLineItem))
        );
    }

    @DisplayName("주문 항목 없을 시 주문 불가능")
    @Test
    void createOrderFailWhenNoItem() {
        // given
        Order request = new Order(1L, Collections.emptyList());

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 항목과 메뉴 숫자가 다른 경우(메뉴에 없는 주문 항목) 주문 불가능")
    @Test
    void createOrderFailWhenItemNotExists() {
        // given
        OrderLineItem requestOrderLineItem = new OrderLineItem(1L, 1);

        Mockito.when(menuDao.countByIdIn(Mockito.anyList()))
            .thenReturn(2L);

        Order request = new Order(1L, Collections.singletonList(requestOrderLineItem));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 테이블이 빈 테이블인 경우 주문 불가능")
    @Test
    void createOrderFailWhenTableNotExists() {
        // given
        OrderLineItem requestOrderLineItem = new OrderLineItem(1L, 1);

        Mockito.when(menuDao.countByIdIn(Mockito.anyList()))
            .thenReturn(1L);

        OrderTable orderTable = new OrderTable(1L, 1L, 4, true);
        Mockito.when(orderTableDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));

        Order request = new Order(1L, Collections.singletonList(requestOrderLineItem));

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 조회")
    @Test
    void list() {
        // given
        Order order = new Order(1L, 1L, "COOKING", LocalDateTime.now(), new ArrayList<>());
        Mockito.when(orderDao.findAll())
            .thenReturn(Collections.singletonList(order));

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        Mockito.when(orderLineItemDao.findAllByOrderId(Mockito.anyLong()))
            .thenReturn(Collections.singletonList(orderLineItem));
        // when
        List<Order> actual = orderService.list();

        // then
        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual.get(0).getId()).isEqualTo(1L),
            () -> assertThat(actual.get(0).getOrderTableId()).isEqualTo(1L),
            () -> assertThat(actual.get(0).getOrderStatus()).isEqualTo("COOKING"),
            () -> assertThat(actual.get(0).getOrderLineItems()).isEqualTo(Collections.singletonList(orderLineItem))
        );
    }

    @DisplayName("주문 상태변경")
    @Test
    void changeOrderStatus() {
        // given
        Order order = new Order(1L, 1L, "COOKING", LocalDateTime.now(), new ArrayList<>());
        Mockito.when(orderDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(order));

        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        Mockito.when(orderLineItemDao.findAllByOrderId(Mockito.anyLong()))
            .thenReturn(Collections.singletonList(orderLineItem));

        Order request = new Order();
        request.setOrderStatus("MEAL");

        // when
        Order actual = orderService.changeOrderStatus(1L, request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
        Mockito.verify(orderDao).save(Mockito.any());
    }

    @DisplayName("이미 완료상태이면 변경 불가능")
    @Test
    void modifyOrderStatusFailWhenAlreadyCompleted() {
        // given
        Order order = new Order(1L, 1L, "COMPLETION", LocalDateTime.now(), new ArrayList<>());
        Mockito.when(orderDao.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(order));

        Order request = new Order();
        request.setOrderStatus("MEAL");

        // when and then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(1L, request));
    }
}