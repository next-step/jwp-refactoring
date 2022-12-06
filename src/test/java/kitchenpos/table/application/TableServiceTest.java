package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.table.application.TableService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 서비스")
@SpringBootTest
class TableServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("손님수를 변경한다. / 0명보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_minimum() {

        Long orderTableId = 1L;

        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
    }

    @DisplayName("손님수를 변경한다. / 주문테이블이 없을 수 없다.")
    @Test
    void changeNumberOfGuests_fail_notExistOrderTable() {
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님수를 변경한다. / 테이블이 공석 상태면 손님수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuest_fail_notEmptyTable() {

        final OrderTable savedOrderTable = orderTableDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(savedOrderTable.isEmpty()).isTrue();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new ChangeNumberOfGuestsRequest(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("공석 상태로 변경한다. / 테이블 그룹이 있을 수 없다.")
    @Test
    void changeEmpty_fail_notTableGroup() {

        OrderTable orderTable = orderTableDao.save(new OrderTable());
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable);
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(orderTables));

        final OrderTable savedOrderTable = orderTableDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.setTableGroupId(tableGroup.getId());

        orderTableDao.save(savedOrderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
    }
}
