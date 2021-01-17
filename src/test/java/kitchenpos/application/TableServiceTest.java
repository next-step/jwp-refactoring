package kitchenpos.application;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends BaseTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        List<OrderTable> orderTables = tableService.list();
        OrderTable expected = orderTables.get(0);

        OrderTable actual = tableService.create(expected);

        assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
        assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
        List<OrderTable> orderTables = tableService.list();
        OrderTable expected = orderTables.get(1);

        OrderTable actual = tableService.changeEmpty(expected.getId(), expected);

        assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 단체 지정인 경우")
    @Test
    void validExistTableGroup() {
        List<OrderTable> orderTables = tableService.list();
        OrderTable expected = orderTables.get(0);
        createtableGroup(orderTables);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(expected.getId(), expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님의 수 변경")
    @Test
    void changeNumberOfGuests() {
        List<OrderTable> orderTables = tableService.list();
        OrderTable expected = orderTables.get(0);
        expected.setNumberOfGuests(10);

        OrderTable actual = tableService.changeNumberOfGuests(expected.getId(), expected);

        assertThat(actual.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("손님의 수 변경 예외 - 0보다 작을 경우")
    @Test
    void validNumberOfGuests() {
        List<OrderTable> orderTables = tableService.list();
        OrderTable expected = orderTables.get(0);
        expected.setNumberOfGuests(-1);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(expected.getId(), expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private void createtableGroup(List<OrderTable> orderTables) {
        TableGroup params = tableGroupDao.findById(1L).get();
        params.setOrderTables(orderTables.subList(1,3));
        tableGroupService.create(params);
    }
}