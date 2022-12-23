package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Product 순살치킨_상품;
    private Product 토마토스파게티_상품;
    private MenuProduct 치킨_두마리;
    private MenuProduct 스파게티_이인분;
    private Menu 치킨_스파게티_더블세트_메뉴;
    private MenuGroup 양식;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_항목;
    private Order 주문;
    OrderLineItemRequest 주문_항목_요청;

    @BeforeEach
    public void setUp() {
        순살치킨_상품 = new Product(1L, "순살치킨", 20_000L);
        토마토스파게티_상품 = new Product(2L, "스파게티", 10_000L);
        양식 = new MenuGroup(1L, new Name("양식"));

        치킨_두마리 = new MenuProduct(1L, new Quantity(2L), 치킨_스파게티_더블세트_메뉴, 순살치킨_상품);
        스파게티_이인분 = new MenuProduct(2L, new Quantity(2L), 치킨_스파게티_더블세트_메뉴, 토마토스파게티_상품);
        List<MenuProduct> menuProducts = Arrays.asList(치킨_두마리, 스파게티_이인분);

        치킨_스파게티_더블세트_메뉴 = Menu.of(1L, "치킨 스파게티 더블세트 메뉴", BigDecimal.valueOf(13_000L), 양식.getId(), menuProducts);

        주문_테이블 = new OrderTable(1L, new NumberOfGuests(1), false);
        주문_항목 = OrderLineItem.of(OrderMenu.of(치킨_스파게티_더블세트_메뉴), 1L);
        주문 = new Order(1L, 주문_테이블.getId(), new OrderLineItems(Arrays.asList(주문_항목)));
        주문.addOrderLineItem(주문_항목);
        주문_항목_요청 = OrderLineItemRequest.of(치킨_스파게티_더블세트_메뉴.getMenuGroupId(), 1L);
    }

    @Test
    void 주문을_등록할_수_있다() {
        OrderRequest request = new OrderRequest(주문_테이블.getId(), Collections.singletonList(주문_항목_요청));
        Order 주문 = Order.of(1L, 주문_테이블.getId(),
                OrderLineItems.of(Collections.singletonList(주문_항목_요청.toOrderLineItem(OrderMenu.of(치킨_스파게티_더블세트_메뉴)))));
        when(menuRepository.findById(치킨_스파게티_더블세트_메뉴.getId())).thenReturn(Optional.of(치킨_스파게티_더블세트_메뉴));
        when(orderTableRepository.findById(request.getOrderTableId())).thenReturn(Optional.of(주문_테이블));
        when(orderRepository.save(any(Order.class))).thenReturn(주문);

        OrderResponse result = orderService.create(request);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(주문.getOrderStatus().name())
        );
    }

    @Test
    void 주문_항목이_비어있는_경우_주문을_등록할_수_없다() {
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 등록되지_않은_주문_항목이_존재_할_경우_주문을_등록할_수_없다() {
        OrderLineItemRequest notExistsOrderItem = OrderLineItemRequest
                .of(치킨_스파게티_더블세트_메뉴.getMenuGroupId(), 1L);
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList(notExistsOrderItem));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 주문_테이블이_등록되어_있지_않은_경우_주문을_등록할_수_없다() {
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(ErrorEnum.NOT_EXISTS_ORDER_TABLE.message());
    }

    @Test
    void 주문_테이블이_비어있는_경우_주문을_등록할_수_없다() {
        주문_테이블.setEmpty(true);
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목_요청));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문));

        List<OrderResponse> orders = orderService.findAll();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.stream().map(OrderResponse::getId)).contains(주문.getId())
        );
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        kitchenpos.order.domain.OrderStatus expectedOrderStatus = kitchenpos.order.domain.OrderStatus.MEAL;
        OrderStatus request = OrderStatus.of(expectedOrderStatus.name());
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderRepository.save(주문)).willReturn(주문);

        OrderResponse result = orderService.changeOrderStatus(주문.getId(), request);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(주문.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(expectedOrderStatus.name())
        );
    }

    @Test
    void 등록되지_않은_주문_상태를_변경할_수_없다() {
        OrderStatus request = OrderStatus.of(kitchenpos.order.domain.OrderStatus.COOKING.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(ErrorEnum.ORDER_TABLE_NOT_FOUND.message());
    }

    @Test
    void 계산_완료된_주문은_상태를_변경할_수_없다() {
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        주문.setOrderStatus(kitchenpos.order.domain.OrderStatus.COMPLETION);

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.of(주문.getOrderStatus().name())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ORDER_COMPLETION_STATUS_NOT_CHANGE.message());
    }
}
