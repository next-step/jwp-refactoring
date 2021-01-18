package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 서비스에 관련한 기능")
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TableService tableService;

    @DisplayName("`주문`을 생성한다.")
    @Test
    void createOrder() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(menuParams));

        // When
        OrderResponse actual = orderService.create(orderRequest);

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(actual.getOrderLineItems()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderedTime()).isNotNull()
        );
    }

    @DisplayName("`주문`에 필요한 주문 메뉴를 담은 `주문 항목`이 없으면 `주문`을 생성할 수 없다.")
    @Test
    void exceptionToCreateOrderWithoutLineItem() {
        // When & then
        assertThatThrownBy(() -> orderService.create(new OrderRequest())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문`할 `주문 테이블`이 없으면, `주문`을 생성할 수 없다.")
    @Test
    void exceptionToCreateOrderWithoutTable() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        long invalidMenuProductId = Long.MAX_VALUE;
        OrderRequest orderRequest = new OrderRequest(invalidMenuProductId, Collections.singletonList(menuParams));

        // When & Then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 `주문` 목록을 조회한다.")
    @Test
    void findAllOrders() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(menuParams));
        OrderResponse expected = orderService.create(orderRequest);

        // When
        List<OrderResponse> actual = orderService.list();

        // Then
        assertThat(actual).containsAnyElementsOf(Collections.singletonList(expected));
    }

    @DisplayName("`주문`의 `주문 상태`를 변경한다.")
    @Test
    void changeOrderStatus() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(menuParams));
        OrderResponse savedOrder = orderService.create(orderRequest);
        OrderRequest updateOrder = new OrderRequest(OrderStatus.COMPLETION.name());

        // When
        Order actual = orderService.changeOrderStatus(savedOrder.getId(), updateOrder);

        // Then
        assertThat(actual.getOrderStatus()).isEqualTo(updateOrder.getOrderStatus());
    }

    @DisplayName("`주문 상태`가 'COMPLETION' 인 경우 상태를 변경할 수 없다.")
    @Test
    void exceptionToChangeOrderStatusWithCompletion() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(menuParams));
        OrderResponse savedOrder = orderService.create(orderRequest);

        OrderRequest updateOrder = new OrderRequest(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), updateOrder);

        // When & Then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), new OrderRequest(OrderStatus.MEAL.name())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
