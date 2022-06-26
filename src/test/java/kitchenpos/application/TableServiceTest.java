package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = 테이블_생성(1L, 0, true);
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        //given
        given(orderTableDao.save(any())).willReturn(orderTable);

        //when
        OrderTable createdOrderTable = tableService.create(orderTable);

        //then
        assertThat(createdOrderTable).isNotNull();
        assertThat(createdOrderTable.getId()).isEqualTo(orderTable.getId());
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable));

        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables).hasSize(1);
        assertThat(orderTables).containsExactly(orderTable);
    }

    @DisplayName("테이블의 빈 테이블 여부를 변경한다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);
        orderTable.setTableGroupId(null);
        orderTable.setEmpty(newOrderTable.isEmpty());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(orderTable);

        //when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), newOrderTable);

        //then
        assertThat(changedOrderTable).isNotNull();
        assertThat(changedOrderTable.isEmpty()).isEqualTo(newOrderTable.isEmpty());
    }

    @DisplayName("존재하지 않는 테이블의 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_invalidEmptyTable() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(null));

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 속해 있는 테이블의 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_invalidHasTableGroup() {
        //given
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리 또는 식사중인 테이블의 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_invalidOrderStatus() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any())).willReturn(orderTable);

        //when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable);

        //then
        assertThat(changedOrderTable).isNotNull();
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(newOrderTable.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를 0 미만으로 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidMinusGuests() {
        //given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(-1);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidNotExistsTable() {
        //given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(4);
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(null));

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_invalidEmptyTable() {
        //given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(4);
        orderTable.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 테이블_생성(Long id, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(id);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
