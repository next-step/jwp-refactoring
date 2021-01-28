package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TableService tableService;
    @Autowired
    private MenuService menuService;

    private Menu createdMenu;
    private OrderTable createdOrderTable;
    private OrderTable createdOrderTable2;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("양식");
        MenuGroup createMenuGroup = menuGroupService.create(menuGroup);

        Product product = new Product();
        product.setName("알리오올리오");
        product.setPrice(BigDecimal.valueOf(12_000));
        Product createdProduct = productService.create(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(createdProduct.getId());
        menuProduct.setQuantity(1L);

        Menu menu = new Menu();
        menu.setName("알리오올리오");
        menu.setPrice(BigDecimal.valueOf(12_000));
        menu.setMenuGroupId(createMenuGroup.getId());
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        createdMenu = menuService.create(menu);

        OrderTable orderTable = new OrderTable();
        createdOrderTable = tableService.create(orderTable);

        OrderTable orderTable2 = new OrderTable();
        createdOrderTable2 = tableService.create(orderTable);

    }

    @Test
    @DisplayName("order 생성")
    void order_create_test() {
        //given
        OrderLineItem orderLineItem = ORDER_LINE_ITEM_생성(createdMenu.getId());
        Order orderRequest = ORDER_REQUEST_생성(createdOrderTable, orderLineItem);

        //when
        Order createdOrder = ORDER_생성_테스트(orderRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdOrder.getId()).isNotNull();
        });
    }

    @Test
    @DisplayName("order에는 order line item이 1개 이상 존재해야한다.")
    void order_create_order_item_null_test() {
        //given
        Order orderRequest = ORDER_REQUEST_생성(createdOrderTable, ORDER_LINE_ITEM_생성(createdMenu.getId()));
        orderRequest.setOrderLineItems(Collections.emptyList());

        //when
        //then
        assertThatThrownBy(() -> {
            Order createdOrder = ORDER_생성_테스트(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("order에는 order table이 1개 이상 존재해야한다.")
    void order_create_order_table_null_test() {
        //given
        Order orderRequest = ORDER_REQUEST_생성(createdOrderTable, ORDER_LINE_ITEM_생성(createdMenu.getId()));
        orderRequest.setOrderLineItems(Collections.emptyList());

        //when
        //then
        assertThatThrownBy(() -> {
            Order createdOrder = ORDER_생성_테스트(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("order 리스트 조회")
    void order_show_test() {
        //given
        OrderLineItem orderLineItem1 = ORDER_LINE_ITEM_생성(createdMenu.getId());
        Order order1 = ORDER_생성_테스트(ORDER_REQUEST_생성(createdOrderTable, orderLineItem1));
        OrderLineItem orderLineItem2 = ORDER_LINE_ITEM_생성(createdMenu.getId());
        Order order2 = ORDER_생성_테스트(ORDER_REQUEST_생성(createdOrderTable2, orderLineItem2));

        //when
        List<Order> list = orderService.list();

        //then
        Assertions.assertAll(() -> {
            List<Long> collect = list.stream().map(Order::getId).collect(Collectors.toList());
            List<Long> orders = Arrays.asList(order1.getId(), order2.getId());
            assertThat(collect).containsAll(orders);

            List<OrderLineItem> collect1 = list.stream()
                .flatMap(order -> order.getOrderLineItems().stream())
                .collect(Collectors.toList());

            long count = collect1.stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderLineItem1.getOrderId()) &&
                    orderLineItem.getMenuId().equals(orderLineItem1.getMenuId()))
                .count();
            assertThat(count).isGreaterThan(0L);

            long count2 = collect1.stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(orderLineItem2.getOrderId()) &&
                    orderLineItem.getMenuId().equals(orderLineItem2.getMenuId()))
                .count();
            assertThat(count2).isGreaterThan(0L);
        });

    }


    private OrderLineItem ORDER_LINE_ITEM_생성(long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        return orderLineItem;
    }


    private Order ORDER_생성_테스트(Order orderRequest) {
        return orderService.create(orderRequest);
    }

    private Order ORDER_REQUEST_생성(OrderTable createdOrderTable, OrderLineItem orderLineItem) {
        Order orderRequest = new Order();
        orderRequest.setOrderLineItems(Collections.singletonList(orderLineItem));
        orderRequest.setOrderTableId(createdOrderTable.getId());
        return orderRequest;
    }
}
