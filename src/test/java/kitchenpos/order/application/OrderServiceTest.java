package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.TableRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public
class OrderServiceTest {

    public static final Long ORDER_TABLE_ID = 1L;
    public static final List<OrderLineItemRequest> ORDER_LINE_ITEMS = Arrays.asList(
        new OrderLineItemRequest(1L, 2L),
        new OrderLineItemRequest(2L, 1L)
    );

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void 주문_등록_테스트() {
        // when
        OrderResponse actual = 주문_등록();

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getOrderTable()).isNotNull();
        assertThat(actual.getOrderStatus()).isNotNull();
        assertThat(actual.getOrderedTime()).isNotNull();
        assertThat(actual.getOrderLineItems().size()).isEqualTo(ORDER_LINE_ITEMS.size());
    }

    @DisplayName("주문 항목이 비어 있으면 주문을 등록할 수 없다.")
    @Test
    void 주문_등록_예외_주문_항목_없음() {
        // given
        List<OrderLineItemRequest> orderLineItems = Lists.emptyList();
        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, orderLineItems);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.create(request)
        );
    }

    @DisplayName("등록되어 있는 주문 테이블이 아니면 주문을 등록할 수 없다.")
    @Test
    void 주문_등록_예외_주문_테이블_없음() {
        // given
        Long unregisterTableId = 10L;
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
            new OrderLineItemRequest(1L, 2L),
            new OrderLineItemRequest(2L, 1L)
        );
        OrderRequest request = new OrderRequest(unregisterTableId, OrderStatus.COOKING, orderLineItems);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.create(request)
        );
    }

    @DisplayName("주문 테이블이 빈 테이블이면 등록할 수 없다.")
    @Test
    void 주문_등록_예외_빈_테이블() {
        // given
        OrderRequest request = new OrderRequest(ORDER_TABLE_ID, OrderStatus.COOKING, ORDER_LINE_ITEMS);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.create(request)
        );
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void 주문목록_조회() {
        // given
        OrderResponse expected = 주문_등록();

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(expected);
    }

    @DisplayName("주문을 등록 후 메뉴 정보가 변경되어도 주문 항목은 변경되지 않는다.")
    @Test
    void 주문_등록_후_메뉴_정보_변경() {
        // given
        Long targetMenuId = 1L;
        OrderResponse registeredOrder = 주문_등록();
        String 기존_메뉴명 = 주문_항목_가져오기(registeredOrder, targetMenuId).getName();
        MenuResponse changedMenu = 메뉴_정보_변경(targetMenuId);

        // when
        OrderResponse actual = 주문_정보_조회(registeredOrder.getId());

        // then
        assertThat(actual.getId()).isEqualTo(registeredOrder.getId());
        assertThat(hasMenuName(actual.getOrderLineItems(), 기존_메뉴명)).isTrue();
        assertThat(hasMenuName(actual.getOrderLineItems(), changedMenu.getName())).isFalse();
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void 주문상태_변경(String status) {
        // given
        OrderStatus 상태 = OrderStatus.valueOf(status);
        OrderResponse 주문 = 주문_등록();

        // when
        OrderResponse actual = 주문_상태_변경(주문.getId(), 상태);

        // then
        assertThat(actual.getId()).isEqualTo(주문.getId());
        assertThat(actual.getOrderStatus()).isEqualTo(상태);

        if (상태.equals(OrderStatus.COMPLETION)) {
            assertThat(actual.getOrderTable().isEmpty()).isTrue();
        }
    }

    @DisplayName("주문이 COMPLETION 상태일 때는 주문 변경을 할 수 없다.")
    @Test
    void 주문상태_변경_예외() {
        // given
        OrderResponse 주문 = 주문_등록();
        주문_상태_변경(주문.getId(), OrderStatus.COMPLETION);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> 주문_상태_변경(주문.getId(), OrderStatus.MEAL)
        );
    }

    private OrderResponse 주문_등록() {
        tableService.changeEmpty(ORDER_TABLE_ID, new TableRequest(5, false));
        OrderRequest request = new OrderRequest(ORDER_TABLE_ID, OrderStatus.COOKING, ORDER_LINE_ITEMS);
        return orderService.create(request);
    }

    private OrderResponse 주문_상태_변경(Long orderId, OrderStatus 주문_상태) {
        OrderRequest request = new OrderRequest(ORDER_TABLE_ID, 주문_상태, ORDER_LINE_ITEMS);
        return orderService.changeOrderStatus(orderId, request);
    }

    private MenuResponse 메뉴_정보_변경(Long targetMenuId) {
        return menuService.update(targetMenuId,
            new MenuRequest("후라이드치킨이아니고후후치킨", BigDecimal.valueOf(18000), null, null));
    }

    private OrderResponse 주문_정보_조회(Long orderId) {
        return orderService.list()
            .stream()
            .filter(order -> order.getId().equals(orderId))
            .findFirst()
            .get();
    }

    private OrderLineItem 주문_항목_가져오기(OrderResponse order, Long menuId) {
        return order.getOrderLineItems().stream()
            .filter(item -> item.getMenuId().equals(menuId))
            .findFirst()
            .get();
    }

    private boolean hasMenuName(List<OrderLineItem> orderLineItems, String menuName) {
        return orderLineItems.stream()
            .anyMatch(item -> item.getName().equals(menuName));
    }
}
