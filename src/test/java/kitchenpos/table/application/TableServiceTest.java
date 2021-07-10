package kitchenpos.table.application;

import java.util.Optional;
import kitchenpos.handler.exception.NotChangeEmptyException;
import kitchenpos.handler.exception.NotChangeNumberOfGuestsException;
import kitchenpos.handler.exception.NotFoundEntityException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.CreateOrderTableDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock 
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private Long orderTableId;
    private Long tableGroupId;

    @BeforeEach
    void setUp() {
        orderTableId = 1L;
        tableGroupId = 1L;
    }

    @DisplayName("생성 성공")
    @Test
    void createSuccess() {
        // given
        CreateOrderTableDto orderTableDto = new CreateOrderTableDto();

        OrderTable orderTable = new OrderTable(orderTableDto.getNumberOfGuests(), orderTableDto.isEmpty());
        given(orderTableRepository.save(any())).willReturn(orderTable);

        // when
        OrderTable actual = tableService.create(orderTableDto);

        // then
        assertNull(actual.getTableGroupId());
    }

    @DisplayName("empty 상태 변경 실패 - 찾을 수 없는 주문테이블")
    @Test
    void changeEmptyFail01() {
        // given
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        // when
        assertThatExceptionOfType(NotFoundEntityException.class).isThrownBy(() -> tableService.changeEmpty(orderTableId, true));
    }

    @DisplayName("empty 상태 변경 실패 - table group에 속한 order table")
    @Test
    void changeEmptyFail02() {
        // given
        OrderTable orderTable = new OrderTable(1L, tableGroupId, 0, true);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        assertThatExceptionOfType(NotChangeEmptyException.class).isThrownBy(() -> tableService.changeEmpty(orderTableId, true));
    }

    @DisplayName("empty 상태 변경 실패 - 주문의 상태가 COOKING 또는 MEAL")
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL" })
    @ParameterizedTest
    void changeEmptyFail03(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.notEmpty();

        Order order = new Order();
        order.changeOrderStatus(orderStatus);
        orderTable.addOrder(order);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        assertThatExceptionOfType(
            NotChangeEmptyException.class).isThrownBy(() -> tableService.changeEmpty(orderTableId, true));
    }

    @DisplayName("empty 상태 변경 성공")
    @Test
    void changeEmptySuccess() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        tableService.changeEmpty(orderTable.getId(), true);
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 손님 수가 0 미만")
    @ValueSource(ints = { -1, -500, -999999 })
    @ParameterizedTest
    void changeNumberOfGuestsFail01(int numberOfGuests) {
        assertThatExceptionOfType(NotChangeNumberOfGuestsException.class).isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId, numberOfGuests));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 찾을 수 없는 주문 테이블")
    @Test
    void changeNumberOfGuestsFail02() {
        // given
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        // when
        assertThatExceptionOfType(NotFoundEntityException.class).isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, 3));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - empty 상태인 주문 테이블은 손님 수 변경 불가")
    @Test
    void changeNumberOfGuestsFail03() {
        // given
        OrderTable orderTable = new OrderTable(orderTableId, null, 3, true);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        assertThatExceptionOfType(NotChangeNumberOfGuestsException.class).isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId, 3));
    }

    @DisplayName("주문 테이블의 손님 수 변경 성공")
    @Test
    void changeNumberOfGuestsSuccess() {
        // given
        OrderTable orderTable = new OrderTable(orderTableId, null, 3, false);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), 3);

        // then
        assertEquals(orderTable.getNumberOfGuests(), actual.getNumberOfGuests());
    }
}
