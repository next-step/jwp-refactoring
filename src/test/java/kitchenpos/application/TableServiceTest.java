package kitchenpos.application;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private Long orderTableId;

    @BeforeEach
    void setUp() {
        orderTableId = 1L;
    }

    @DisplayName("생성 성공")
    @Test
    void createSuccess() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(3L);

        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertNull(actual.getTableGroupId());
    }

    @DisplayName("empty 상태 변경 실패 - 찾을 수 없는 orderTableId")
    @Test
    void changeEmptyFail01() {
        // given
        OrderTable orderTable = new OrderTable();

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable));
    }

    @DisplayName("empty 상태 변경 실패 - table group에 속한 order table")
    @Test
    void changeEmptyFail02() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable));
    }

    @DisplayName("empty 상태 변경 실패 - 주문의 상태가 COOKING 또는 MEAL")
    @Test
    void changeEmptyFail03() {
        // given
        OrderTable orderTable = new OrderTable();

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                                                            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(true);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable));
    }

    @DisplayName("empty 상태 변경 실패 - 주문의 상태가 COOKING 또는 MEAL")
    @Test
    void changeEmptySuccess() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                                                            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);

        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeEmpty(orderTableId, orderTable);
        assertEquals(orderTable.isEmpty(), actual.isEmpty());
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 손님 수가 0 이하")
    @ValueSource(ints = { 0, -1, -500, -999999 })
    @ParameterizedTest
    void changeNumberOfGuestsFail01(int numberOfGuests) {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 찾을 수 없는 orderTableId")
    @Test
    void changeNumberOfGuestsFail02() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - empty 상태인 주문 테이블은 손님 수 변경 불가")
    @Test
    void changeNumberOfGuestsFail03() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable));
    }

    @DisplayName("주문 테이블의 손님 수 변경 성공")
    @Test
    void changeNumberOfGuestsSuccess() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(orderTableId, orderTable);

        // then
        assertEquals(orderTable.getNumberOfGuests(), actual.getNumberOfGuests());
    }
}
