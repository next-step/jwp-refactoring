package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 1, false);
        orderTable1 = new OrderTable(2L, null, 1, false);
    }

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        // when
        OrderTable createdOrderTable = tableService.create(orderTable1);

        // then
        assertThat(createdOrderTable.getId()).isEqualTo(orderTable1.getId());
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(createdOrderTable.isEmpty()).isEqualTo(orderTable1.isEmpty());
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다.")
    @Test
    void changeEmptyTest() {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable1.getId(), orderTable1);

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할시 이미 등록된 주문 테이블 이어야 한다.")
    @Test
    void changeEmptyTest_wrongOrderTable() {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할시 단체 지정에 등록되지 않은 주문테이블 이어야 한다.")
    @Test
    void changeEmptyTest_wrongOrderTable2() {
        // given
        orderTable1.setTableGroupId(1L);
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할시 주문의 상태가 완료 이어야 한다.")
    @Test
    void changeEmptyTest_wrongOrderTable3() {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
    }

    @DisplayName("주문 테이블의 손님수를 변경시 손님수는 0명 이상이어야 한다.")
    @Test
    void changeNumberOfGuestsTest_wrongNumberOfGuests() {
        // given
        orderTable1.setNumberOfGuests(-1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님수를 변경시 등록된 주문 테이블 이어야 한다.")
    @Test
    void changeNumberOfGuestsTest_unregisteredOrderTable() {
        // given
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님수를 변경시 빈 주문 테이블이 아니어야 한다.")
    @Test
    void changeNumberOfGuestsTest_emptyOrderTable() {
        // given
        orderTable1.setEmpty(true);
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
    }
}
