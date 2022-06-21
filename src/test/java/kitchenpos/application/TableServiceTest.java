package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest{

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        OrderTable orderTable = this.tableService.create(new OrderTable(4, false));

        assertThat(orderTable.getId()).isNotNull();
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("모든 테이블을 조회한다.")
    void list() {
        OrderTable orderTable1 = this.tableService.create(new OrderTable(4, false));
        OrderTable orderTable2 = this.tableService.create(new OrderTable(4, false));

        this.orderTableDao.save(orderTable1);
        this.orderTableDao.save(orderTable2);

        assertThat(this.tableService.list()).containsExactly(orderTable1, orderTable2);
    }

    @Test
    @DisplayName("테이블 그룹에 포함될 경우 테이블의 빈 상태를 바꿀 수 없다.")
    void changeEmptyFail_existTableGroup() {
        TableGroup tableGroup = this.tableGroupDao.save(new TableGroup(null));
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(tableGroup.getId(), 4, false));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeEmpty(orderTable.getId(), null));
    }

    @Test
    @DisplayName("주문 상태가 식사중이거나 조리중인 테이블이 있다면 빈 상태를 바꿀 수 없다.")
    void changeEmptyFail_existOrderCookingOrMeal() {
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(4, false));
        Order order = new Order(orderTable.getId(), null);
        order.setOrderStatus(OrderStatus.COOKING.name());
        this.orderDao.save(order);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeEmpty(orderTable.getId(), null));
    }

    @Test
    @DisplayName("테이블의 빈 상태가 변경된다.")
    void changeEmpty() {
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(4, false));
        OrderTable statusOrderTable = new OrderTable();
        statusOrderTable.setEmpty(true);

        OrderTable expectedOrderTable = this.tableService.changeEmpty(orderTable.getId(), statusOrderTable);

        assertThat(expectedOrderTable.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("변경하고자 하는 손님의 숫자가 음수일 경우 에러를 던진다.")
    void changeNumberOfGuestsFail_numberOfGuests() {
        OrderTable statusOrderTable = new OrderTable();
        statusOrderTable.setEmpty(true);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeNumberOfGuests(1L, statusOrderTable));
    }

    @Test
    @DisplayName("변경하고자 하는 테이블이 없을 경우 에러를 던진다.")
    void changeNumberOfGuestsFail_orderTable() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.tableService.changeNumberOfGuests(1L, new OrderTable()));
    }

    @Test
    @DisplayName("테이블의 인원수를 변경한다.")
    void changeNumberOfGuests() {
        OrderTable orderTable = this.orderTableDao.save(new OrderTable(4, false));
        OrderTable statusOrderTable = new OrderTable();
        statusOrderTable.setNumberOfGuests(3);

        OrderTable expectedOrderTable = this.tableService.changeNumberOfGuests(orderTable.getId(), statusOrderTable);

        assertThat(expectedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

}
