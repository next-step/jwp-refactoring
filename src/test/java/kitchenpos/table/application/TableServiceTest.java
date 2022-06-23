package kitchenpos.table.application;

import static kitchenpos.ServiceTestFactory.createOrderTableBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dao.FakeOrderDao;
import kitchenpos.table.dao.FakeOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceTest {
    private final TableService tableService = new TableService(new FakeOrderDao(), new FakeOrderTableDao());
    private OrderTable firstTable;
    private OrderTable secondTable;
    private OrderTable thirdTable;
    private OrderTable emptyTable;

    @BeforeEach
    void setUp() {
        firstTable = createOrderTableBy(1L, 4, false, 1L);
        secondTable = createOrderTableBy(2L, 3, false, null);
        thirdTable = createOrderTableBy(3L, 2, false, null);
        emptyTable = createOrderTableBy(4L, 0, true, null);
    }

    @Test
    @DisplayName("테이블을 등록한다.")
    void create() {
        //when
        OrderTable saved = tableService.create(firstTable);
        //then
        assertThat(saved.getTableGroupId()).isNull();
        assertThat(saved.getNumberOfGuests()).isEqualTo(firstTable.getNumberOfGuests());
        assertThat(saved.isEmpty()).isEqualTo(firstTable.isEmpty());
    }

    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void findAll() {
        //given
        tableService.create(firstTable);
        //when
        List<OrderTable> tables = tableService.list();
        //then
        assertThat(tables).hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 테이블의 빈 테이블 여부를 수정하려고 하면 예외를 반환한다.")
    void changeEmptyWithNoExistingOrderTable() {
        //when, then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(99L, firstTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 이미 단체 지정되어있으면 예외를 반환한다.")
    void changeEmptyWithGroupedOrderTable() {
        //given
        OrderTable saved = tableService.create(firstTable);

        //when, then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(saved.getId(), saved);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 조리, 식사 중이면 예외를 반환한다.")
    void changeEmptyWithOrderStatus() {
        //given
        tableService.create(firstTable);
        tableService.create(secondTable);
        OrderTable saved = tableService.create(thirdTable);
        //when, then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(saved.getId(), saved);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 빈 테이블 여부가 수정된다.")
    void changeEmpty() {
        //given
        tableService.create(firstTable);
        OrderTable saved = tableService.create(secondTable);
        //when
        OrderTable changed = tableService.changeEmpty(saved.getId(), emptyTable);
        //then
        assertThat(changed.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("0명 미만으로 손님수를 수정할 수 없다.")
    void changeNumberOfGuestsWithInvalidNumber() {
        //given
        OrderTable saved = tableService.create(firstTable);
        OrderTable orderTable = createOrderTableBy(99L, -1, false, null);
        //when, then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(saved.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블은 손님수를 수정할 수 없다.")
    void changeNumberOfGuestsWithEmptyTable() {
        //given
        tableService.create(firstTable);
        tableService.create(secondTable);
        tableService.create(thirdTable);
        OrderTable saved = tableService.create(emptyTable);

        //when, then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(saved.getId(), firstTable);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("손님수를 수정한다.")
    void changeNumber() {
        //given
        OrderTable saved = tableService.create(firstTable);
        //when
        OrderTable changed = tableService.changeNumberOfGuests(saved.getId(), firstTable);
        //then
        assertThat(changed.getNumberOfGuests()).isEqualTo(firstTable.getNumberOfGuests());
    }
}
