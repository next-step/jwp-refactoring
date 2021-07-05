package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Price;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderLineItemDao orderLineItemDao;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    MenuGroup savedMenuGroup;

    Menu savedMenu;

    OrderTable savedOrderTable;

    OrderTable savedEmptyOrderTable;

    OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup("패스트푸드");
        savedMenuGroup = menuGroupRepository.save(menuGroup);

        Menu menu = new Menu("맥도날드햄버거", new Price(BigDecimal.valueOf(10000)), savedMenuGroup.getId());

        savedMenu = menuRepository.save(menu);

        OrderTable orderTable = new OrderTable(2, false);

        savedOrderTable = orderTableRepository.save(orderTable);

        OrderTable emptyOrderTable = new OrderTable(0, false);

        emptyOrderTable.setEmpty(true);
        savedEmptyOrderTable = orderTableRepository.save(emptyOrderTable);

        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1);
    }

    @DisplayName("주문을 생성하자")
    @Test
    public void createOrder() throws Exception {
        //given
        String orderStatus = OrderStatus.COOKING.name();

        OrderRequest order = new OrderRequest();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(Arrays.asList(new OrderLineItemRequest(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity())));

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertNotNull(savedOrder.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(savedOrder.getOrderTableId()).isEqualTo(savedOrderTable.getId());
        assertThat(savedOrder.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 목록을 출력해보자자")
    @Test
    public void listMenuGroup() throws Exception {
        //given
        LocalDateTime orderedTime = LocalDateTime.of(2021, 7, 1, 01, 10, 00);

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(Arrays.asList(orderLineItem));
        order.setOrderedTime(orderedTime);
        Order savedOrder = orderDao.save(order);

        //when
        List<Order> orders = orderService.list();
        List<Long> findOrderIds = orders.stream()
                .map(findOrder -> findOrder.getId())
                .collect(Collectors.toList());
        //then
        assertNotNull(orders);
        assertTrue(findOrderIds.contains(savedOrder.getId()));
    }

    @DisplayName("메뉴가 없을때는 주문생성에 실패한다.")
    @Test
    public void failCreateOrderEmptyOrderLineItems() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(savedOrderTable.getId());
        orderRequest.setOrderStatus(OrderStatus.COOKING.name());
        orderRequest.setOrderedTime(LocalDateTime.now());

        //when
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 번호가 존재하지 않을경우 주문생성에 실패한다.")
    @Test
    public void failCreateOrderInvalidOrderTableId() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(0L);
        orderRequest.setOrderStatus(OrderStatus.COOKING.name());
        orderRequest.setOrderedTime(LocalDateTime.now());
//        order.setOrderLineItems(Arrays.asList(orderLineItem));
        orderRequest.setOrderLineItems(Arrays.asList(new OrderLineItemRequest(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity())));

        //when
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 비어있는 경우에 주문생성에 실패한다.")
    @Test
    public void failCreateOrderEmptyOrderTableId() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(savedEmptyOrderTable.getId());
        orderRequest.setOrderStatus(OrderStatus.COOKING.name());
        orderRequest.setOrderedTime(LocalDateTime.now());
//        order.setOrderLineItems(Arrays.asList(orderLineItem));
        orderRequest.setOrderLineItems(Arrays.asList(new OrderLineItemRequest(orderLineItem.getSeq(), orderLineItem.getOrderId(), orderLineItem.getMenuId(), orderLineItem.getQuantity())));

        //when
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 변경해보자")
    @Test
    public void changeOrderStatus() throws Exception {
        //given
        LocalDateTime orderedTime = LocalDateTime.of(2021, 7, 1, 01, 10, 00);

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(orderedTime);

        Order savedOrder = orderDao.save(order);

        String orderStatusMeal = OrderStatus.MEAL.name();
        order.setOrderStatus(orderStatusMeal);

        //when
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), order);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatusMeal);
        assertThat(changedOrder.getOrderedTime()).isEqualTo(orderedTime);
    }

    @DisplayName("존재하지 않는 주문을 변경할수는 없다.")
    @Test
    public void failChangeOrderStatusNotExistOrderId() throws Exception {
        //then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(0L, new Order())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 주문의 상태를 변경할수는 없다.")
    @Test
    public void couldNotChangeOrderStatus() throws Exception {
        // given
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());

        Order savedOrder = orderDao.save(order);

        order.setOrderStatus(OrderStatus.MEAL.name());
        //then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(savedOrder.getId(), order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
