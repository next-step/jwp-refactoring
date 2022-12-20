package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.order.application.OrderService;
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
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(2, false));
        Product product1 = productService.create(new ProductRequest("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new ProductRequest("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new MenuRequest("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProductRequest(product1.getId(), 1),
                new MenuProductRequest(product2.getId(), 1)
        )));
        OrderRequest order = new OrderRequest(orderTable1.getId()
                , Collections.singletonList(new OrderLineItemRequest(menu1.getId(), 1)));
        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문한 메뉴가 없으면 주문에 실패한다.")
    void createFailTest1(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(2, false));
        OrderRequest order = new OrderRequest(orderTable1.getId(), Collections.emptyList());

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
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(2, false));
        OrderRequest order = new OrderRequest(orderTable1.getId(),
                Collections.singletonList(new OrderLineItemRequest(9999L, 1)));

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
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(2, true));
        Product product1 = productService.create(new ProductRequest("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new ProductRequest("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new MenuRequest("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProductRequest(product1.getId(), 1),
                new MenuProductRequest(product2.getId(), 1)
        )));
        OrderRequest order = new OrderRequest(orderTable1.getId(),
                Collections.singletonList(new OrderLineItemRequest(menu1.getId(), 1)));

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
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(2, false));
        Product product1 = productService.create(new ProductRequest("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new ProductRequest("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new MenuRequest("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProductRequest(product1.getId(), 1),
                new MenuProductRequest(product2.getId(), 1)
        )));

        Order order = orderService.create(new OrderRequest(orderTable1.getId(),
                Collections.singletonList(new OrderLineItemRequest(menu1.getId(), 1))));

        // when
        Order changedOrder = orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(OrderStatus.MEAL.name()));

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("주문 상태를 식사중 에서 계산완료로 변경한다.")
    void changeStatus2(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(2, false));
        Product product1 = productService.create(new ProductRequest("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new ProductRequest("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new MenuRequest("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProductRequest(product1.getId(), 1),
                new MenuProductRequest(product2.getId(), 1)
        )));

        Order order = orderService.create(new OrderRequest(orderTable1.getId(),
                Collections.singletonList(new OrderLineItemRequest(menu1.getId(), 1))));

        // when
        Order changedOrder = orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(OrderStatus.COMPLETION.name()));

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("주문 상태가 계산완료면, 상태를 변경 할 수 없다.")
    void changeStatusFail(){
        // given
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(2, false));
        Product product1 = productService.create(new ProductRequest("상품1", new BigDecimal(1000)));
        Product product2 = productService.create(new ProductRequest("상품2", new BigDecimal(2000)));
        MenuGroup group1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu menu1 = menuService.create(new MenuRequest("메뉴1", new BigDecimal(1000), group1.getId(), Arrays.asList(
                new MenuProductRequest(product1.getId(), 1),
                new MenuProductRequest(product2.getId(), 1)
        )));

        Order order = orderService.create(new OrderRequest(orderTable1.getId(),
                Collections.singletonList(new OrderLineItemRequest(menu1.getId(), 1))));
        orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(OrderStatus.COMPLETION.name()));

        // when
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(OrderStatus.MEAL.name()))
        );
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), new OrderStatusRequest(OrderStatus.COOKING.name()))
        );

        // then
    }
}