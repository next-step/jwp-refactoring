package kitchenpos.table.application;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock 
    private OrderRepository orderRepository;
    
    @Mock 
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private Long orderTableId;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTableId = 1L;
        tableGroup = new TableGroup();
    }

    @DisplayName("생성 성공")
    @Test
    void createSuccess() {
        // given
        OrderTableDto orderTableDto = new OrderTableDto();

        OrderTable orderTable = new OrderTable(orderTableDto.getNumberOfGuests(), orderTableDto.isEmpty());
        given(orderTableRepository.save(any())).willReturn(orderTable);

        // when
        OrderTable actual = tableService.create(orderTableDto);

        // then
        assertNull(actual.getTableGroup());
    }

    @DisplayName("empty 상태 변경 실패 - 찾을 수 없는 orderTable.getId()")
    @Test
    void changeEmptyFail01() {
        // given
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, true));
    }

    @DisplayName("empty 상태 변경 실패 - table group에 속한 order table")
    @Test
    void changeEmptyFail02() {
        // given
        OrderTable orderTable = new OrderTable(1L, tableGroup, 0, true);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, true));
    }

    @DisplayName("empty 상태 변경 실패 - 주문의 상태가 COOKING 또는 MEAL")
    @Test
    void changeEmptyFail03() {
        // given
        OrderTable orderTable = new OrderTable();

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                                                                   Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .willReturn(true);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, true));
    }

    @DisplayName("empty 상태 변경 실패 - 주문의 상태가 COOKING 또는 MEAL")
    @Test
    void changeEmptySuccess() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 0, true);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                                                                   Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .willReturn(false);

        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeEmpty(orderTable.getId(), true);
        assertEquals(orderTable.isEmpty(), actual.isEmpty());
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 손님 수가 0 이하")
    @ValueSource(ints = { 0, -1, -500, -999999 })
    @ParameterizedTest
    void changeNumberOfGuestsFail01(int numberOfGuests) {
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, numberOfGuests));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 찾을 수 없는 orderTable.getId()")
    @Test
    void changeNumberOfGuestsFail02() {
        // given
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, 3));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - empty 상태인 주문 테이블은 손님 수 변경 불가")
    @Test
    void changeNumberOfGuestsFail03() {
        // given
        OrderTable orderTable = new OrderTable(orderTableId, null, 3, true);
        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, 3));
    }

    @DisplayName("주문 테이블의 손님 수 변경 성공")
    @Test
    void changeNumberOfGuestsSuccess() {
        // given
        OrderTable orderTable = new OrderTable(orderTableId, null, 3, false);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), 3);

        // then
        assertEquals(orderTable.getNumberOfGuests(), actual.getNumberOfGuests());
    }
}
