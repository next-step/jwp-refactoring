package kitchenpos.table.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 관련 기능 테스트")
public class OrderTableTest {
    private TableGroup tableGroup;
    private Menu menu;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup(1L);

        orderTable = 주문테이블_생성(1L, tableGroup, 3, false);
        orderLineItem = 주문_목록_생성(1L, 1L, 3L);
    }
    @DisplayName("주문 테이블 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = 주문_테이블_생성_요청(1L, tableGroup, 2, false);
        주문_테이블_생성됨(orderTable);
    }

    @DisplayName("주문 테이블 생성시 인원은 0명 이상이어야 한다.")
    @Test
    void create_예외() {
        주문테이블_생성시_인원이_0명_이하일_경우_예외_발생함(-1);
    }

    @DisplayName("비어있는 테이블은 인원수 변경이 불가합니다.")
    @Test
    void changeNumberOfGuest_예외1() {
        OrderTable orderTable = 주문_테이블_생성_요청(1L, tableGroup, 0, true);
        비어있는_테이블_인원수_변경시_예외_발생함(orderTable, 3);
    }

    @DisplayName("그룹으로 묶여있는 경우 테이블을 비을 수 없습니다.")
    @Test
    void emptyOrderTable_예외() {
        OrderTable orderTable = 주문_테이블_생성_요청(1L, tableGroup, 2, false);
        Order order = 주문_생성_요청(orderTable, OrderStatus.COOKING);

        그룹화된_테이블_비울경우_예외_발생(orderTable, order);

    }

    private OrderTable 주문테이블_생성(Long id, TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuest, empty);
    }

    private OrderLineItem 주문_목록_생성(Long seq, Long menuId, Long quantity) {
        return new OrderLineItem(seq, menuId, quantity);
    }

    private Order 주문_생성_요청(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(orderTable.getId(), orderStatus);
    }

    private void 주문_테이블_생성됨(OrderTable orderTable) {
        assertThat(orderTable).isNotNull();
        assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroup.getId());
        assertThat(orderTable.getEmpty()).isFalse();
    }

    private OrderTable 주문_테이블_생성_요청(Long id, TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuest, empty);
    }

    private void 주문테이블_생성시_인원이_0명_이하일_경우_예외_발생함(int numberOfQuest) {
        assertThatThrownBy(() -> {
            new OrderTable(1L, tableGroup, numberOfQuest, false);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("변경하려는 인원수는 0보다 커야합니다.");
    }

    private void 비어있는_테이블_인원수_변경시_예외_발생함(OrderTable orderTable, int numberOfGuest) {
        assertThatThrownBy(() -> {
            orderTable.updateNumberOfGuests(numberOfGuest);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있는 테이블은 인원수 변경이 불가합니다.");
    }

    private void 그룹화된_테이블_비울경우_예외_발생(OrderTable orderTable, Order order) {
        assertThatThrownBy(() -> {
            orderTable.updateEmpty(true);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("룹으로 묶여있는 경우 테이블을 비을 수 없습니다.");
    }

    private void 주문_테이블_비워짐(OrderTable orderTable) {
        assertThat(orderTable.getEmpty()).isTrue();
    }
}
