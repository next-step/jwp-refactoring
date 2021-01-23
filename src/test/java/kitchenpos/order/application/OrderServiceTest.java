package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class OrderServiceTest {
    private Menu 아메리카노_케익;
    private Menu 아메리카노_샌드위치;
    private OrderTable 주문테이블;

    @Autowired
    OrderService orderService;

    @Autowired
    TableService orderTableService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    OrderLineItemRepository orderLineItemRepository;

    @BeforeEach
    void setUp() {
        MenuGroup 커피류 = menuGroupRepository.save(new MenuGroup("커피류"));
        아메리카노_케익 = menuRepository.save(new Menu("아메리카노_케익", BigDecimal.valueOf(10000), 커피류));
        아메리카노_샌드위치 = menuRepository.save(new Menu("아메리카노_샌드위치", BigDecimal.valueOf(10000), 커피류));
        주문테이블 = orderTableRepository.save(new OrderTable(0, false));
    }

    @DisplayName("주문 등록")
    @Test
    void create() {
        OrderResponse 주문 = createOrder();
        assertThat(주문.getOrderTableId()).isEqualTo(주문테이블.getId());
    }


    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderResponse 주문 = createOrder();
        OrderRequest request = new OrderRequest(OrderStatus.MEAL.name());
        OrderResponse response = orderService.changeOrderStatus(주문.getId(), request);

        assertThat(response.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }

    @DisplayName("주문 상태 변경 예외 - 이미 완료된 상태 변경할 경우")
    @Test
    void validCopletionOrderStatus() {
        OrderResponse 주문 = createOrder();
        OrderRequest request = new OrderRequest(OrderStatus.COMPLETION.name());
        OrderResponse response = orderService.changeOrderStatus(주문.getId(), request);

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderResponse createOrder() {
        OrderLineItemRequest 주문항목1 = new OrderLineItemRequest(아메리카노_케익.getId(), 1);
        OrderLineItemRequest 주문항목2 = new OrderLineItemRequest(아메리카노_샌드위치.getId(), 1);
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(주문항목1);
        orderLineItemRequests.add(주문항목2);
        OrderRequest request = new OrderRequest(주문테이블.getId(), orderLineItemRequests);
        return orderService.create(request);
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 요리중이거나 식사중인 경우")
    @Test
    void validOrderStatusCookingOrMeal() {
        OrderLineItemRequest 주문항목1 = new OrderLineItemRequest(아메리카노_케익.getId(), 1);
        OrderLineItemRequest 주문항목2 = new OrderLineItemRequest(아메리카노_샌드위치.getId(), 1);
        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(주문항목1);
        orderLineItemRequests.add(주문항목2);
        OrderRequest request = new OrderRequest(주문테이블.getId(), orderLineItemRequests, "COOKING");
        orderService.create(request);

        assertThatThrownBy(()-> {
            orderTableService.changeEmpty(주문테이블.getId(), new OrderTableRequest(0, true));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
