package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.List;
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
}
