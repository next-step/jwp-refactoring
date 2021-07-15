package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블을 관리한다.")
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문 테이블들을 조회할수 있다.")
    @Test
    void listTest() {
        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).isNotNull();
    }

    @DisplayName("주문 테이블을 등록할수 있다.")
    @Test
    void createTest() {
        // when
        OrderTable actualOrderTable = tableService.create(
            new OrderTable(1, true)
        );

        // then
        assertThat(actualOrderTable).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("주문 테이블의 비어있는 상태값을 수정할수 있다.")
    @ValueSource(booleans = {true, false})
    void changeEmptyTest(boolean isEmpty) {
        // when
        OrderTable actualOrderTable = tableService.changeEmpty(
            1L, new OrderTable(1, isEmpty)
        );

        // then
        assertThat(actualOrderTable).isNotNull();
        assertThat(actualOrderTable.isEmpty()).isEqualTo(isEmpty);
    }

    @DisplayName("주문 테이블의 비어있는 상태값을 수정시, 등록된 주문 테이블만 상태값을 수정할 수 있다.")
    @Test
    void changeEmptyExceptionTest1() {
        // given
        long orderTableId = 999L;

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, new OrderTable(1, false)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록된").hasMessageContaining("테이블만");
    }

    @DisplayName("주문 테이블의 비어있는 상태값을 수정시, 그룹 설정이 되어 있는 주문테이블은 상태를 바꿀 수 없다.")
    @Test
    void changeEmptyExceptionTest2() {
        // given
        OrderTable orderTable1 = tableService.create(
            new OrderTable(1, true)
        );

        OrderTable orderTable2 = tableService.create(
            new OrderTable(1, true)
        );

        TableGroup tableGroup = new TableGroup(TestUtils.getRandomId(), orderTable1, orderTable2);
        tableGroupService.create(tableGroup);

        // then
        assertThatThrownBy(() ->
            tableService.changeEmpty(orderTable1.getId(), new OrderTable(1, true)
        )).isInstanceOf(RuntimeException.class).hasMessageContaining("그룹 설정이 되어 있는");
    }

    @ParameterizedTest
    @DisplayName("주문 테이블의 비어있는 상태값을 수정시, 주문테이블의 주문이 `조리` 상태, `식사` 상태이면 주문 테이블 상태를 바꿀 수 없다.")
    @EnumSource(value = Order.OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmptyExceptionTest3(Order.OrderStatus orderStatus) {
        //given
        Menu menu = menuService.list().get(0);
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );


        Order savedOrder = orderService.create(new Order(orderTable.getId(), OrderLineItem.valueOf(menu.getId(), 1L)));
        savedOrder.changeOrderStatus(orderStatus);
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining(orderStatus.remark())
            .hasMessageContaining("변경");
    }

    @ParameterizedTest
    @DisplayName("주문 테이블의 손님수를 수정할수 있다.")
    @ValueSource(ints = {0, 1, 10})
    void changeNumberOfGuestsTest(int numberOfGuests) {
        // given
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        // when
        OrderTable actualOrderTable = tableService.changeNumberOfGuests(
            orderTable.getId(), new OrderTable(numberOfGuests, false)
        );

        // then
        assertThat(actualOrderTable).isNotNull();
        assertThat(actualOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("주문 테이블의 손님수를 수정시, 손님 수는 1명 이상만 가능하다.")
    @Test
    void changeNumberOfGuestsExceptionTes1() {
        // given
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
            orderTable.getId(), new OrderTable(-1, false)
        )).isInstanceOf(RuntimeException.class).hasMessageContaining("음수");
    }

    @DisplayName("주문 테이블의 손님수를 수정시, 등록된 주문 테이블만 손님 수를 수정할 수 있다.")
    @Test
    void changeNumberOfGuestsExceptionTest2() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
            999L, new OrderTable(1, false)
        )).isInstanceOf(RuntimeException.class).hasMessageContaining("등록된 주문 테이블만");
    }

    @DisplayName("주문 테이블의 손님수를 수정시, 비워있지 않은 주문 테이블만 손님 수를 수정할 수 있다.")
    @Test
    void changeNumberOfGuestsExceptionTest3() {
        // given
        OrderTable orderTable = tableService.create(
            new OrderTable(1, true)
        );

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
            orderTable.getId(), new OrderTable(1, false)
        )).isInstanceOf(RuntimeException.class)
            .hasMessageContaining("비어있어")
            .hasMessageContaining("불가능");
    }
}