package kitchenpos.order.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.order.domain.OrderTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("그룹화 관련 기능 테스트")
public class TableGroupTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 3, true);
        orderTable2= new OrderTable(2L, null, 5, true);
    }

    @DisplayName("그룹화 한다.")
    @Test
    void group() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup tableGroup = 그룹화_요청(orderTables);

        그룹화_생성됨(tableGroup);
    }

    @DisplayName("주문테이블 2개 미만인 경우 그룹화 예외 발생")
    @Test
    void group_exception() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1);

        주문테이블_2개_미만인_경우_그룹화_예외_발생함(orderTables);
    }

    @DisplayName("그룹으로 묶여있는 주문 테이블 그룹화 할 경우 예외 발생")
    @Test
    void group_exception2() {
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, tableGroup, 3, true);
        OrderTable orderTable2 = new OrderTable(1L, tableGroup, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        그룹으로_묶인_주문_테이블_그룹화_예외_발생함(orderTables);
    }

    @DisplayName("주문의 상태가 조리 또는 식사인 경우 그룹을 해제할 수 없다.")
    @Test
    void ungroup_exception() {
        TableGroup tableGroup = 테이블_그룹_생성됨();

        Order createdOrder = 주문_생성됨(OrderStatus.COOKING);

        주문상태_완료가_아닐경우_그룹해제_예외_발생함(tableGroup, createdOrder);
    }

    @DisplayName("그룹을 해제한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = 테이블_그룹_생성됨();

        Order createdOrder = 주문_생성됨(OrderStatus.COMPLETION);

        그룹_해제_요청(tableGroup, createdOrder);

        그룹_해제됨(tableGroup);
    }

    private TableGroup 테이블_그룹_생성됨() {
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(1L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        return new TableGroup(1L, orderTables);
    }

    private Order 주문_생성됨(OrderStatus orderStatus) {
        Menu menu = new Menu(1L, new Name("뿌링클치즈볼"), new Price(new BigDecimal(18000)), new MenuGroup());
        OrderLineItem orderLineItem = 주문_목록_생성(1L, menu, 3L);
        return 주문_생성_요청(orderTable1, orderStatus, Arrays.asList(orderLineItem));
    }

    private TableGroup 그룹화_요청(List<OrderTable> orderTables) {
        return new TableGroup(1L, orderTables);
    }

    private void 그룹화_생성됨(TableGroup tableGroup) {
        assertThat(tableGroup.getOrderTables()).hasSize(2);
        assertThat(tableGroup.getOrderTables().get(0).getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(tableGroup.getOrderTables().get(1).getNumberOfGuests()).isEqualTo(orderTable2.getNumberOfGuests());
    }

    private void 주문테이블_2개_미만인_경우_그룹화_예외_발생함(List<OrderTable> orderTables) {
        assertThatThrownBy(() -> {
            new TableGroup(1L, orderTables);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹테이블은 2개 이상이어야 그룹화가 가능합니다.");
    }

    private void 그룹으로_묶인_주문_테이블_그룹화_예외_발생함(List<OrderTable> orderTables) {
        assertThatThrownBy(() -> {
            new TableGroup(2L, orderTables);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 그룹으로 묶여있는 경우 그룹화 할 수 없습니다.");
    }


    private void 주문상태_완료가_아닐경우_그룹해제_예외_발생함(TableGroup tableGroup, Order createdOrder) {
        assertThatThrownBy(() -> {
            tableGroup.ungroupOrderTables(Arrays.asList(createdOrder));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.");
    }

    private void 그룹_해제_요청(TableGroup tableGroup, Order createdOrder) {
        tableGroup.ungroupOrderTables(Arrays.asList(createdOrder));
    }

    private void 그룹_해제됨(TableGroup tableGroup) {
        assertThat(tableGroup.getOrderTables()).hasSize(0);
    }
}
