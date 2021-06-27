package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 클래스")
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("주문이 주어지면")
        class Context_with_order {
            final Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                OrderLineItem orderLineItem1 = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                givenOrder.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
                OrderTable orderTable = new OrderTable();
                orderTable.setEmpty(false);

                when(menuDao.countByIdIn(anyList()))
                        .thenReturn(2L);
                when(orderTableDao.findById(any()))
                        .thenReturn(Optional.of(orderTable));
                when(orderDao.save(any(Order.class)))
                        .thenReturn(givenOrder);
            }

            @Test
            @DisplayName("주어진 주문을 저장하고, 저장된 객체를 리턴한다.")
            void it_returns_saved_order() {
                Order actual = orderService.create(givenOrder);

                assertThat(actual).isEqualTo(givenOrder);
            }
        }

        @Nested
        @DisplayName("주문과 비어있는 주문 항목 목록이 주어지면")
        class Context_with_order_and_empty_order_lines {
            final Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                givenOrder.setOrderLineItems(new ArrayList<>());
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> orderService.create(givenOrder))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문 항목 갯수와 해당하는 메뉴의 갯수가 다르게 주어지면")
        class Context_with_different_order_lines_size_and_menu_size {
            final Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                OrderLineItem orderLineItem1 = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                givenOrder.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
                when(menuDao.countByIdIn(anyList()))
                        .thenReturn(1L);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> orderService.create(givenOrder))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문이 주문 테이블이 존재하지 않은 상태로 주어지면")
        class Context_with_not_exist_order_table {
            final Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                OrderLineItem orderLineItem1 = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                givenOrder.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
                OrderTable orderTable = new OrderTable();
                orderTable.setEmpty(false);

                when(menuDao.countByIdIn(anyList()))
                        .thenReturn(2L);
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> orderService.create(givenOrder))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("주문이 주문 테이블이 비어있는 상태로 주어지면")
        class Context_with_empty_order_table {
            final Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                OrderLineItem orderLineItem1 = new OrderLineItem();
                OrderLineItem orderLineItem2 = new OrderLineItem();
                givenOrder.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
                OrderTable orderTable = new OrderTable();
                orderTable.setEmpty(true);

                when(menuDao.countByIdIn(anyList()))
                        .thenReturn(2L);
                when(orderTableDao.findById(any()))
                        .thenReturn(Optional.of(orderTable));
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> orderService.create(givenOrder))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("저장된 주문 목록이 주어지면")
        class Context_with_orders {
            final Order givenOrder1 = new Order();
            final Order givenOrder2 = new Order();

            @BeforeEach
            void setUp() {
                when(orderDao.findAll())
                        .thenReturn(Arrays.asList(givenOrder1, givenOrder2));
            }

            @Test
            @DisplayName("주목 목록을 리턴한다.")
            void it_returns_orders() {
                List<Order> actual = orderService.list();
                assertThat(actual).containsExactly(givenOrder1, givenOrder2);
            }
        }
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드는")
    class Describe_changeOrderStatus {

        @Nested
        @DisplayName("주문 식별자와 주문상태가 담긴 주문이 주어지면")
        class Context_with_orders {
            final Long givenOrderId = 1L;
            final Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                givenOrder.setOrderStatus(OrderStatus.MEAL.name());

                Order savedOrder = new Order();
                savedOrder.setOrderStatus(OrderStatus.COOKING.name());
                when(orderDao.findById(anyLong()))
                        .thenReturn(Optional.of(savedOrder));
                when(orderDao.save(any(Order.class)))
                        .thenReturn(givenOrder);
            }

            @Test
            @DisplayName("변경된 주문상태를 저장하고, 갱신된 주문 정보를 리턴한다.")
            void it_returns_orders() {
                Order actual = orderService.changeOrderStatus(givenOrderId, givenOrder);
                assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
            }
        }

        @Nested
        @DisplayName("저장된 주문 상태가 완료가 주어지면")
        class Context_with_order_status_completed {
            final Long givenOrderId = 1L;
            final Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                givenOrder.setOrderStatus(OrderStatus.MEAL.name());

                Order savedOrder = new Order();
                savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
                when(orderDao.findById(anyLong()))
                        .thenReturn(Optional.of(savedOrder));
            }

            @Test
            @DisplayName("예외를 던진다.")
            void it_throws_exception() {
                assertThatThrownBy(() -> orderService.changeOrderStatus(givenOrderId, givenOrder))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
