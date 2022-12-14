package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.UpdateOrderStatusRequest;
import kitchenpos.order.repository.OrderRepository;
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

    private Product 치킨;
    private Product 스파게티;
    private MenuProduct 치킨_두마리;
    private MenuProduct 스파게티_이인분;
    private Menu 치킨_스파게티_더블세트_메뉴;
    private MenuGroup 양식;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_항목;
    private Order 주문;
    private List<Long> menuIds;

    @BeforeEach
    void setUp() {
        치킨 = new Product(1L, "치킨", BigDecimal.valueOf(20_000));
        스파게티 = new Product(2L, "스파게티", BigDecimal.valueOf(10_000));
        치킨_두마리 = new MenuProduct(1L, 2L, 치킨_스파게티_더블세트_메뉴, 치킨);
        스파게티_이인분 = new MenuProduct(2L, 2L, 치킨_스파게티_더블세트_메뉴, 스파게티);
        치킨_스파게티_더블세트_메뉴 = new Menu(1L, "치킨 스파게티 더블세트 메뉴", new BigDecimal(13_000), 양식);

        주문_테이블 = new OrderTable(1L, 0, false);
        주문 = new Order(주문_테이블, OrderStatus.COOKING, LocalDateTime.now(), new ArrayList<>());
        주문_항목 = new OrderLineItem(1L, 치킨_스파게티_더블세트_메뉴.getId(), 1);
        주문.setOrderLineItems(Collections.singletonList(주문_항목));

        menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @Test
    void 주문을_등록할_수_있다() {
        given(menuRepository.countByIdIn(menuIds)).willReturn(menuIds.size());
        given(orderTableRepository.findById(주문.getOrderTable().getId())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.save(주문)).willReturn(주문);
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목));

        OrderResponse result = orderService.create(request);

        assertAll(
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(result.getOrderLineItems()).contains(주문_항목),
                () -> assertThat(result.getOrderedTime()).isNotNull()
        );
    }

    @Test
    void 주문_항목이_비어있는_경우_주문을_등록할_수_없다() {
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되지_않은_주문_항목이_존재_할_경우_주문을_등록할_수_없다() {
        given(menuRepository.countByIdIn(menuIds)).willReturn(menuIds.size());
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_등록되어_있지_않은_경우_주문을_등록할_수_없다() {
        given(menuRepository.countByIdIn(menuIds)).willReturn(menuIds.size());
        given(orderTableRepository.findById(주문.getOrderTable().getId())).willReturn(Optional.empty());
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList(주문_항목));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있는_경우_주문을_등록할_수_없다() {
        주문_테이블.setEmpty(true);
        OrderRequest request = OrderRequest.of(주문_테이블.getId(), Arrays.asList());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.stream().map(OrderResponse::getId)).contains(주문.getId())
        );
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderStatus expectedOrderStatus = OrderStatus.MEAL;
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(expectedOrderStatus.name());
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
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(OrderStatus.COOKING.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 계산_완료된_주문은_상태를_변경할_수_없다() {
        주문.setOrderStatus(OrderStatus.COMPLETION);
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.of(주문.getOrderStatus().name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
