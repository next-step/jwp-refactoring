package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.domain.OrderTest.*;
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
        orderLineItem = 주문_목록_생성(1L, menu, 3L);
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
        Order order = 주문_생성_요청(orderTable, OrderStatus.COOKING, Arrays.asList(orderLineItem));

        그룹화된_테이블_비울경우_예외_발생(orderTable, order);

    }

    @DisplayName("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.")
    @Test
    void emptyOrderTable_예외2() {
        OrderTable orderTable = new OrderTable(2, false);
        Order order = 주문_생성_요청(orderTable, OrderStatus.COOKING, Arrays.asList(orderLineItem));

        주문_상태가_조리_혹은_식사일때_테이블_비울경우_예외_발생함(orderTable, order);
    }

    @DisplayName("테이블 비우기")
    @Test
    void empty() {
        OrderTable orderTable = new OrderTable(2, false);
        Order order = 주문_생성_요청(orderTable, OrderStatus.COMPLETION, Arrays.asList(orderLineItem));

        주문_테이블_비우기_요청(orderTable, order);

        주문_테이블_비워짐(orderTable);
    }

    private void 주문_상태가_조리_혹은_식사일때_테이블_비울경우_예외_발생함(OrderTable orderTable, Order order) {
        assertThatThrownBy(() -> {
            orderTable.updateEmpty(true, Arrays.asList(order));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다");
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
            orderTable.updateEmpty(true, Arrays.asList(order));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("룹으로 묶여있는 경우 테이블을 비을 수 없습니다.");
    }

    private void 주문_테이블_비우기_요청(OrderTable orderTable, Order order) {
        orderTable.updateEmpty(true, Arrays.asList(order));
    }

    private void 주문_테이블_비워짐(OrderTable orderTable) {
        assertThat(orderTable.getEmpty()).isTrue();
    }
}
