package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TableServiceTest {

    @Autowired
    TableService tableService;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    OrderDao orderDao;

    OrderTable orderTable;

    OrderTable savedOrderTable;

    @BeforeEach
    void setUp() {
        int countOfPeople = 4;
        orderTable = new OrderTable();
        orderTable.setNumberOfGuests(countOfPeople);
        orderTable.setEmpty(false);

        savedOrderTable = orderTableDao.save(orderTable);
    }

    @DisplayName("테이블을 생성해보자")
    @Test
    void createTable() {
        // given
        int countOfPeople = 4;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(countOfPeople);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(countOfPeople);
    }

    @Test
    public void tableLists() throws Exception {
        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertNotNull(orderTables);
    }

    @DisplayName("테이블의 상태를 변경하자")
    @Test
    public void changeEmpty() {
        //given
        orderTable.setEmpty(true);
        //when
        OrderTable changedTable = tableService.changeEmpty(savedOrderTable.getId(), orderTable);

        //then
        assertThat(changedTable.getId()).isEqualTo(savedOrderTable.getId());
        assertTrue(changedTable.isEmpty());
    }

    @DisplayName("테이블 그룹이 존재하면 변경 실패한다.")
    @Test
    public void failChangeEmptyExistTableGroup() throws Exception {
        //given
        // 테이블그룹 추가
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(savedOrderTable));
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());

        tableGroupDao.save(tableGroup);

        //when
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedOrderTable.getTableGroupId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태가 요리중이거나 식사중이면 변경 실패한다.")
    @Test
    public void failChangeEmptyBecauseStatusForCookingAndMeal() throws Exception {
        //given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        //when
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedOrderTable.getTableGroupId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님의 숫자를 변경하자")
    @Test
    public void changeNumberOfGuests() throws Exception {
        //given
        int changedPeopleCount = 2;
        orderTable.setNumberOfGuests(changedPeopleCount);

        //when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

        //then
        assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(changedPeopleCount);
    }

    @DisplayName("테이블의 손님수가 0보다 작을수는 없다")
    @Test
    public void failChangeNumberOfGuestsInvalidPeopleCount() throws Exception {
        //given
        int changedPeopleCount = -1;

        orderTable.setNumberOfGuests(changedPeopleCount);

        //when
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블일 경우는 손님수를 변경할수 없다")
    @Test
    public void failChangeNumberOfGuestsNotEmptyOrderTable() throws Exception {
        //given
        int emptyPeopleCount = 0;
        int countOfPeople = 4;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(emptyPeopleCount);
        orderTable.setEmpty(true);

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        orderTable.setNumberOfGuests(countOfPeople);
        //when
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
