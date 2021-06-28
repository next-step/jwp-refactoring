package kitchenpos.table.application;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
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
    private OrderDao orderDao;
    
    @Mock 
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
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
        OrderTableDto orderTableDto = new OrderTableDto(1L, 1L, 0, true);
        given(orderTableRepository.findById(orderTableDto.getId())).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableDto.getId(), orderTableDto));
    }

    @DisplayName("empty 상태 변경 실패 - table group에 속한 order table")
    @Test
    void changeEmptyFail02() {
        // given
        OrderTableDto orderTableDto = new OrderTableDto(1L, 1L, 0, true);
        OrderTable orderTable = new OrderTable(1L, tableGroup, 0, true);

        given(orderTableRepository.findById(orderTableDto.getId())).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableDto));
    }

    @DisplayName("empty 상태 변경 실패 - 주문의 상태가 COOKING 또는 MEAL")
    @Test
    void changeEmptyFail03() {
        // given
        OrderTableDto orderTableDto = new OrderTableDto();
        OrderTable orderTable = new OrderTable();

        given(orderTableRepository.findById(orderTableDto.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableDto.getId(),
                                                            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(true);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableDto.getId(), orderTableDto));
    }

    @DisplayName("empty 상태 변경 실패 - 주문의 상태가 COOKING 또는 MEAL")
    @Test
    void changeEmptySuccess() {
        // given
        OrderTableDto orderTableDto = new OrderTableDto(null, null, 0, true);
        OrderTable orderTable = new OrderTable(0, true);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                                                            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);

        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTableDto);
        assertEquals(orderTable.isEmpty(), actual.isEmpty());
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 손님 수가 0 이하")
    @ValueSource(ints = { 0, -1, -500, -999999 })
    @ParameterizedTest
    void changeNumberOfGuestsFail01(int numberOfGuests) {
        // given
        OrderTableDto orderTableDto = new OrderTableDto(1L, null, numberOfGuests, false);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableDto.getId(), orderTableDto));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - 찾을 수 없는 orderTable.getId()")
    @Test
    void changeNumberOfGuestsFail02() {
        // given
        OrderTableDto orderTableDto = new OrderTableDto(1L, null, 3, false);
        given(orderTableRepository.findById(orderTableDto.getId())).willReturn(Optional.empty());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableDto.getId(), orderTableDto));
    }

    @DisplayName("주문 테이블의 손님 수 변경 실패 - empty 상태인 주문 테이블은 손님 수 변경 불가")
    @Test
    void changeNumberOfGuestsFail03() {
        // given
        OrderTableDto orderTableDto = new OrderTableDto(1L, null, 3, true);
        OrderTable orderTable = new OrderTable(1L, null, 3, true);
        given(orderTableRepository.findById(orderTableDto.getId())).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableDto.getId(), orderTableDto));
    }

    @DisplayName("주문 테이블의 손님 수 변경 성공")
    @Test
    void changeNumberOfGuestsSuccess() {
        // given
        OrderTableDto orderTableDto = new OrderTableDto(1L, null, 3, false);
        OrderTable orderTable = new OrderTable(1L, null, 3, false);

        given(orderTableRepository.findById(orderTableDto.getId())).willReturn(Optional.of(orderTable));
        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTableDto);

        // then
        assertEquals(orderTable.getNumberOfGuests(), actual.getNumberOfGuests());
    }
}
