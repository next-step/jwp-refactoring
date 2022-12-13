package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuService menuService;

    @Autowired
    TableService tableService;

    @Test
    @DisplayName("주문 성공 테스트")
    void createTest(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, false));
        Product product1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProduct(product1.getId(), 1),
                new MenuProduct(product2.getId(), 1)
        )));
        Order order = new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu1.getId(), 1)));
        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문한 메뉴가 없으면 주문에 실패한다.")
    void createFailTest1(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, false));
        Order order = new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.emptyList());
        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(order)
        );

        // then
    }

    @Test
    @DisplayName("없는 메뉴를 주문하면 주문에 실패한다.")
    void createFailTest2(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, false));
        Order order = new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, 9999L, 1)));
        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(order)
        );

        // then
    }

    @Test
    @DisplayName("비어 있는 테이블에서 주문 시도 시, 주문에 실패한다.")
    void createFailTest3(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, true));
        Product product1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProduct(product1.getId(), 1),
                new MenuProduct(product2.getId(), 1)
        )));
        Order order = new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu1.getId(), 1)));
        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(order)
        );

        // then
    }

    @Test
    @DisplayName("주문 상태를 조리중 에서 식사중으로 변경한다.")
    void changeStatus1(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, false));
        Product product1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProduct(product1.getId(), 1),
                new MenuProduct(product2.getId(), 1)
        )));

        Order order = orderService.create(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu1.getId(), 1))));

        // when
        Order changedOrder = orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.MEAL.name()));

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문 상태를 식사중 에서 계산완료로 변경한다.")
    void changeStatus2(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, false));
        Product product1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProduct(product1.getId(), 1),
                new MenuProduct(product2.getId(), 1)
        )));

        Order order = orderService.create(new Order(orderTable1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu1.getId(), 1))));

        // when
        Order changedOrder = orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.COMPLETION.name()));

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문 상태가 계산완료면, 상태를 변경 할 수 없다.")
    void changeStatusFail(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTable(2, false));
        Product product1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProduct(product1.getId(), 1),
                new MenuProduct(product2.getId(), 1)
        )));

        Order order = orderService.create(new Order(orderTable1.getId(), null, LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu1.getId(), 1))));
        orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.COMPLETION.name()));

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.MEAL.name()))
        );
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.COOKING.name()))
        );

        // then
    }
}