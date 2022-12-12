package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 클래스 테스트")
public class OrderServiceTest {
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

    @Nested
    @DisplayName("create 메서드 테스트")
    public class CreateMethod {
        @Nested
        @DisplayName("주문 생성 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final Order order = setup();

                // when
                final Order createdOrder = orderService.create(order);

                // then
                assertAll(
                        () -> assertThat(createdOrder.getId()).isPositive(),
                        () -> assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                        () -> assertThat(createdOrder.getOrderLineItems()).hasSize(order.getOrderLineItems().size())
                );
            }

            private Order setup() {
                final Order order = new Order();
                order.setId(1L);
                final List<Long> orderLineItemIds = Stream.of(1, 2, 3).map(Long::new).collect(Collectors.toList());
                final List<Long> menuIds = orderLineItemIds.stream().map(id -> id * 2).collect(Collectors.toList());
                final List<OrderLineItem> orderLineItems = orderLineItemIds.stream()
                        .map(id -> {
                            final OrderLineItem orderLineItem = new OrderLineItem();
                            orderLineItem.setSeq(id - 1);
                            orderLineItem.setOrderId(order.getId());
                            orderLineItem.setMenuId(menuIds.get((int) (id - 1)));
                            return orderLineItem;
                        })
                        .collect(Collectors.toList());
                order.setOrderLineItems(orderLineItems);
                Mockito.when(menuDao.countByIdIn(Mockito.anyList())).thenReturn((long) orderLineItems.size());
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(11L);
                orderTable.setEmpty(false);
                order.setOrderTableId(orderTable.getId());
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTable));
                Mockito.when(orderDao.save(Mockito.any())).thenReturn(order);
                Mockito.when(orderLineItemDao.save(Mockito.any()))
                        .thenReturn(orderLineItems.get(0), orderLineItems.get(1), orderLineItems.get(2));
                return order;
            }
        }

        @Nested
        @DisplayName("주문 아이템 리스트가 비어있다면 주문 생성 실패")
        public class ErrorOrderLineItemsEmpty {
            @Test
            public void testCase() {
                // given
                final Order order = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
            }

            private Order setup() {
                final Order order = new Order();
                order.setId(1L);
                final List<Long> orderLineItemIds = Stream.of(1, 2, 3).map(Long::new).collect(Collectors.toList());
                final List<Long> menuIds = orderLineItemIds.stream().map(id -> id * 2).collect(Collectors.toList());
                final List<OrderLineItem> orderLineItems = orderLineItemIds.stream()
                        .map(id -> {
                            final OrderLineItem orderLineItem = new OrderLineItem();
                            orderLineItem.setSeq(id - 1);
                            orderLineItem.setOrderId(order.getId());
                            orderLineItem.setMenuId(menuIds.get((int) (id - 1)));
                            return orderLineItem;
                        })
                        .collect(Collectors.toList());
                order.setOrderLineItems(orderLineItems);
                Mockito.when(menuDao.countByIdIn(Mockito.anyList())).thenReturn((long) orderLineItems.size());
                return order;
            }
        }

        @Nested
        @DisplayName("주문 아이템 개수와 실제로 존재하는 메뉴 개수가 일치하지 않는다면 주문 생성 실패")
        public class ErrorOrderLineItemsCountAndExistingMenuCountNotEqual {
            @Test
            public void testCase() {
                // given
                final Order order = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
            }

            private Order setup() {
                final Order order = new Order();
                order.setId(1L);
                final List<Long> orderLineItemIds = Stream.of(1, 2, 3).map(Long::new).collect(Collectors.toList());
                final List<Long> menuIds = orderLineItemIds.stream().map(id -> id * 2).collect(Collectors.toList());
                final List<OrderLineItem> orderLineItems = orderLineItemIds.stream()
                        .map(id -> {
                            final OrderLineItem orderLineItem = new OrderLineItem();
                            orderLineItem.setSeq(id - 1);
                            orderLineItem.setOrderId(order.getId());
                            orderLineItem.setMenuId(menuIds.get((int) (id - 1)));
                            return orderLineItem;
                        })
                        .collect(Collectors.toList());
                order.setOrderLineItems(orderLineItems);
                Mockito.when(menuDao.countByIdIn(Mockito.anyList())).thenReturn((long) orderLineItems.size() - 1);
                return order;
            }
        }

        @Nested
        @DisplayName("주문 테이블이 비어있다면 주문 생성 실패")
        public class ErrorOrderTableIsEmpty {
            @Test
            public void testCase() {
                // given
                final Order order = setup();

                // when - then
                assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
            }

            private Order setup() {
                final Order order = new Order();
                order.setId(1L);
                final List<Long> orderLineItemIds = Stream.of(1, 2, 3).map(Long::new).collect(Collectors.toList());
                final List<Long> menuIds = orderLineItemIds.stream().map(id -> id * 2).collect(Collectors.toList());
                final List<OrderLineItem> orderLineItems = orderLineItemIds.stream()
                        .map(id -> {
                            final OrderLineItem orderLineItem = new OrderLineItem();
                            orderLineItem.setSeq(id - 1);
                            orderLineItem.setOrderId(order.getId());
                            orderLineItem.setMenuId(menuIds.get((int) (id - 1)));
                            return orderLineItem;
                        })
                        .collect(Collectors.toList());
                order.setOrderLineItems(orderLineItems);
                Mockito.when(menuDao.countByIdIn(Mockito.anyList())).thenReturn((long) orderLineItems.size());
                final OrderTable orderTable = new OrderTable();
                orderTable.setId(11L);
                orderTable.setEmpty(true);
                order.setOrderTableId(orderTable.getId());
                Mockito.when(orderTableDao.findById(Mockito.anyLong())).thenReturn(Optional.of(orderTable));
                return order;
            }
        }
    }

    @Nested
    @DisplayName("list 메서드 테스트")
    public class ListMethod {
        @Nested
        @DisplayName("주문 목록 조회 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final List<Order> orders = setup();

                // when
                final List<Order> gottenOrders = orderService.list();

                // then
                assertThat(gottenOrders).hasSize(orders.size());
            }

            private List<Order> setup() {
                final List<Long> orderIds = Stream.of(1, 2, 3).map(Long::new).collect(Collectors.toList());
                final List<Order> orders = orderIds.stream()
                        .map(id -> {
                            final Order order = new Order();
                            order.setId(id);
                            return order;
                        })
                        .collect(Collectors.toList());
                Mockito.when(orderService.list()).thenReturn(orders);
                return orders;
            }
        }

        @Nested
        @DisplayName("주문이 0 개인 경우에도 목록 조회 성공")
        public class SuccessEmptyList {
            @Test
            public void testCase() {
                // given
                setup();

                // when
                final List<Order> gottenOrders = orderService.list();

                // then
                assertThat(gottenOrders).hasSize(0);
            }

            private void setup() {
                final List<Order> orders = new ArrayList<>();
                Mockito.when(orderService.list()).thenReturn(orders);
            }
        }
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드 테스트")
    public class ChangeOrderStatusMethod {
        @Nested
        @DisplayName("주문 상태 변경 성공")
        public class Success {
            @Test
            public void testCase() {
                // given
                final Order order = setup();
                final Order newOrder = new Order();
                newOrder.setOrderStatus(OrderStatus.MEAL.name());

                // when
                final Order updatedOrder = orderService.changeOrderStatus(order.getId(), newOrder);

                // then
                assertAll(
                        () -> assertThat(updatedOrder.getId()).isEqualTo(order.getId()),
                        () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo(newOrder.getOrderStatus())
                );
            }

            private Order setup() {
                final Order orderBefore = new Order();
                orderBefore.setId(1L);
                orderBefore.setOrderStatus(OrderStatus.COOKING.name());
                Mockito.when(orderDao.findById(Mockito.anyLong())).thenReturn(Optional.of(orderBefore));
                final Order orderAfter = new Order();
                orderAfter.setId(1L);
                orderAfter.setOrderStatus(OrderStatus.MEAL.name());
                Mockito.when(orderDao.save(Mockito.any())).thenReturn(orderAfter);
                return orderBefore;
            }
        }

        @Nested
        @DisplayName("해당 주문 id 로 주문을 찾지 못하는 경우 주문 상태 변경 실패")
        public class ErrorOrderNotFound {
            @Test
            public void testCase() {
                // given
                final Order order = setup();
                final Order newOrder = new Order();
                newOrder.setOrderStatus(OrderStatus.MEAL.name());

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> orderService.changeOrderStatus(order.getId(), newOrder));
            }

            private Order setup() {
                final Order orderBefore = new Order();
                orderBefore.setId(1L);
                orderBefore.setOrderStatus(OrderStatus.COOKING.name());
                Mockito.when(orderDao.findById(Mockito.anyLong())).thenReturn(Optional.empty());
                return orderBefore;
            }
        }

        @Nested
        @DisplayName("이미 결재 완료인 경우 주문 상태 변경 실패")
        public class ErrorOrderAlreadyComplete {
            @Test
            public void testCase() {
                // given
                final Order order = setup();
                final Order newOrder = new Order();
                newOrder.setOrderStatus(OrderStatus.MEAL.name());

                // when - then
                assertThrows(IllegalArgumentException.class,
                        () -> orderService.changeOrderStatus(order.getId(), newOrder));
            }

            private Order setup() {
                final Order orderBefore = new Order();
                orderBefore.setId(1L);
                orderBefore.setOrderStatus(OrderStatus.COMPLETION.name());
                Mockito.when(orderDao.findById(Mockito.anyLong())).thenReturn(Optional.of(orderBefore));
                return orderBefore;
            }
        }
    }
}
