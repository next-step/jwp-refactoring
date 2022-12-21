package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.menu.port.MenuPort;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


    @Mock
    private MenuPort menuPort;
    @Mock
    private OrderPort orderPort;
    @Mock
    private OrderTablePort orderTablePort;
    @InjectMocks
    private OrderService orderService;

    private Product 후라이드치킨;
    private Product 제로콜라;
    private MenuGroup 치킨;
    private MenuProduct 후라이드_이인분;
    private MenuProduct 제로콜라_삼인분;
    private Menu 후치콜세트;
    private OrderTable 주문테이블;

    private OrderLineItem 주문_항목;

    private Order 주문;

    private OrderLineItemRequest 주문요청;

    @BeforeEach
    void setUp() {
        후라이드치킨 = new Product(new Price(BigDecimal.valueOf(3_000)), "후라이드치킨");
        제로콜라 = new Product(new Price(BigDecimal.valueOf(2_000)), "제로콜라");

        치킨 = new MenuGroup("치킨");

        후치콜세트 = new Menu(1L, "후치콜세트", new Price(BigDecimal.valueOf(5_000)), 치킨.getId());

        후라이드_이인분 = new MenuProduct(후치콜세트, 후라이드치킨, 2);
        제로콜라_삼인분 = new MenuProduct(후치콜세트, 제로콜라, 2);

        주문테이블 = new OrderTable(1L, null, 0, false);
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING, null);
        주문_항목 = new OrderLineItem(후치콜세트, 1L);
        주문.addOrderLineItems(Arrays.asList(주문_항목), Arrays.asList(후치콜세트));
        주문요청 = new OrderLineItemRequest(후치콜세트.getId(), 1L);
    }

    @Test
    @DisplayName("주문을 등록 할 수 있다")
    void createOrder() {
        given(orderTablePort.findById(any())).willReturn(주문테이블);
        given(menuPort.findAllByMenuId(any())).willReturn(Arrays.asList(후치콜세트));
        given(orderPort.save(any())).willReturn(주문);


        OrderRequest request = new OrderRequest(1L, Arrays.asList(주문요청));


        OrderResponse result = orderService.create(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getOrderStatus()).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 항목이 비어 있을 수 없다.")
    void createOrderEmpty() {
        assertThatThrownBy(() ->
                orderService.create(new OrderRequest(1L, null))
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("주문 리스트를 받을 수 있다.")
    void getOrderList() {
        Order 주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING, null);
        OrderLineItem 주문_항목 = new OrderLineItem(1L, 주문, 후치콜세트, 1L);
        주문.addOrderLineItems(Arrays.asList(주문_항목), Arrays.asList(후치콜세트));

        given(orderPort.findAll()).willReturn(Collections.singletonList(주문));

        List<OrderResponse> result = orderService.list();

        assertThat(result).hasSize(1);

        assertThat(result.stream().map(OrderResponse::getId)).contains(주문.getId());
    }

    @Test
    @DisplayName("주문 상태를 변경 할 수 있다.")
    void changeOrderStatus() {
        given(orderPort.findById(1L)).willReturn(주문);

        orderService.changeOrderStatus(주문.getId(), new ChangeOrderStatusRequest(OrderStatus.COMPLETION));

        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("계산이 완료된 상태이면 주문 상태 변경이 불가능하다.")
    void changeOrderStatusNotCompleteChange() {
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COMPLETION, null);
        주문_항목 = new OrderLineItem(후치콜세트, 1L);
        주문.addOrderLineItems(Arrays.asList(주문_항목), Arrays.asList(후치콜세트));
        주문요청 = new OrderLineItemRequest(후치콜세트.getId(), 1L);


        given(orderPort.findById(any())).willReturn(주문);

        assertThatThrownBy(() ->
                orderService.changeOrderStatus(주문.getId(), new ChangeOrderStatusRequest(OrderStatus.COOKING))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
