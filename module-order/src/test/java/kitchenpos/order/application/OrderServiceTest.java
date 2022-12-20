package kitchenpos.order.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuServiceTestSupport;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService service;

    private Menu 강정치킨;
    private OrderTable 테이블;
    private OrderTable 빈테이블;

    @BeforeEach
    public void setUp(@Autowired MenuServiceTestSupport menuServiceTestSupport,
                      @Autowired OrderTableRepository orderTableRepository) {
        강정치킨 = menuServiceTestSupport.신메뉴_강정치킨_가져오기();
        테이블 = orderTableRepository.save(new OrderTable(5, false));
        빈테이블 = orderTableRepository.save(new OrderTable(0, true));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(강정치킨.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(테이블.getId(), Arrays.asList(orderLineItemRequest));

        OrderResponse orderResponse = service.create(orderRequest);

        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orderResponse.getOrderLineItems()).element(0).satisfies(item -> {
                    assertThat(item.getOrderId()).isNotNull();
                })
        );
    }

    @DisplayName("주문 항목없이 주문을 생성한다.")
    @Test
    void createWithEmptyItems() {
        OrderRequest orderRequest = new OrderRequest(테이블.getId(), Collections.emptyList());

        assertThatThrownBy(() -> {
            service.create(orderRequest);
        }).isInstanceOf(EmptyOrderLineItemsException.class)
        .hasMessageContaining("주문 항목이 비었습니다.");
    }

    @DisplayName("없는 메뉴로 주문을 생성한다.")
    @Test
    void createWithNotExistsMenu() {
        Long 존재하지_않는_메뉴_id = Long.MAX_VALUE;
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(존재하지_않는_메뉴_id, 1L);
        OrderRequest orderRequest = new OrderRequest(테이블.getId(), Arrays.asList(orderLineItemRequest));

        assertThatThrownBy(() -> {
            service.create(orderRequest);
        }).isInstanceOf(InvalidOrderException.class)
        .hasMessageContaining("존재하지 않는 메뉴가 있습니다.");
    }

    @DisplayName("빈테이블에 주문을 생성한다.")
    @Test
    void createWithEmptyTable() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(강정치킨.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(빈테이블.getId(), Arrays.asList(orderLineItemRequest));

        assertThatThrownBy(() -> {
            service.create(orderRequest);
        }).isInstanceOf(InvalidOrderException.class)
        .hasMessageContaining("빈 테이블 입니다.");
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeStatus() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(강정치킨.getId(), 1L);
        OrderResponse orderResponse = service.create(new OrderRequest(테이블.getId(), Arrays.asList(orderLineItemRequest)));

        OrderResponse changeResponse = service.changeOrderStatus(orderResponse.getId(),
                                                                 new OrderStatusRequest(OrderStatus.COMPLETION.name()));

        assertThat(changeResponse.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("존재하지 않는 주문의 주문상태를 변경한다.")
    @Test
    void changeStatusWithNotFoundOrder() {
        Long 존재하지_않는_주문_id = Long.MAX_VALUE;
        assertThatThrownBy(() -> {
            service.changeOrderStatus(존재하지_않는_주문_id, new OrderStatusRequest(OrderStatus.COMPLETION.name()));
        }).isInstanceOf(NotFoundOrderException.class)
        .hasMessageContaining("주문을 찾을 수 없습니다.");
    }
}
