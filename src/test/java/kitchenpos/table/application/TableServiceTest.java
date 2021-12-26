package kitchenpos.table.application;

import kitchenpos.common.exception.*;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static kitchenpos.table.domain.OrderTableTest.빈자리;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 관리 테스트")
public class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTable orderTable;

    private final OrderTableRequest request = new OrderTableRequest(0, false);

    @Test
    @DisplayName("빈 테이블 등록")
    void createEmptyTableTest() {
        // given
        given(orderTableRepository.save(any())).willReturn(빈자리);
        // when
        OrderTableResponse actual = tableService.create(request);
        // then
        verify(orderTableRepository, only()).save(any());
        assertThat(actual).isEqualTo(OrderTableResponse.of(빈자리));
    }

    @Test
    @DisplayName("주문 테이블 리스트 조회")
    void orderTableListTest() {
        // given
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(빈자리));
        // when
        List<OrderTableResponse> actual = tableService.list();
        // then
        verify(orderTableRepository, only()).findAll();
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).containsExactly(OrderTableResponse.of(빈자리))
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "true", "false"
    })
    @DisplayName("테이블 상태 변경")
    void changeEmptyTest(boolean empty) {
        // given
        ChangeEmptyRequest 요청_데이터 = new ChangeEmptyRequest(empty);

        OrderTable savedOrderTable = mock(OrderTable.class);
        given(savedOrderTable.isEmpty()).willReturn(empty);
        given(savedOrderTable.getTableGroup()).willReturn(null);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        // when
        OrderTableResponse actual = tableService.changeEmpty(anyLong(), 요청_데이터);
        // then
        assertThat(actual.isEmpty()).isEqualTo(empty);
    }

    @Test
    @DisplayName("등록되지 않은 주문 테이블은 변경 할 수 없다.")
    void notFoundOrderTableToChangeEmptyTest() {
        // given
        given(orderTableRepository.findByIdElseThrow(anyLong()))
                .willThrow(NotFoundOrderTableException.class);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, any()))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 지정된 주문 테이블은 변경 할 수 없다.")
    void nonNullTableGroupIdTest() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        doThrow(IsNotNullTableGroupException.class).when(savedOrderTable).changeEmpty(anyBoolean());
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new ChangeEmptyRequest(true)))
                .isInstanceOf(IsNotNullTableGroupException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하고, 주문 상태가 `조리(COOKING)`, `식사(MEAL)`상태이면 변경 할 수 없다.")
    void existsByOrderTableIdAndOrderStatusInTest() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, any()))
                .isInstanceOf(InvalidOrderStatusException.class);
        verify(orderRepository, only()).existsByOrderTableIdAndOrderStatusIn(any(), anyList());
    }

    @Test
    @DisplayName("방문한 인원 변경")
    void changeNumberOfGuestsTest() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        given(savedOrderTable.getNumberOfGuests()).willReturn(2);
        given(savedOrderTable.isEmpty()).willReturn(false);
        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(anyLong(), new ChangeGuestsRequest(2));
        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(2);
        verify(orderTable, atMostOnce()).changeNumberOfGuests(any());
    }

    @Test
    @DisplayName("방문한 인원은 0명 이상이어야 한다.")
    void guestsIsOverZeroTest() {
        // given
        ChangeGuestsRequest changeGuestsRequest = new ChangeGuestsRequest(-1);
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeGuestsRequest))
                .isInstanceOf(GuestsNumberNegativeException.class);
    }

    @Test
    @DisplayName("등록되지 않은 주문 테이블은 변경 할 수 없다.")
    void notFoundOrderTableToChangeNumberOfGuestsTest() {
        // given
        given(orderTableRepository.findByIdElseThrow(anyLong())).willThrow(NotFoundOrderTableException.class);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, any()))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("빈 테이블의 인원은 변경 할 수 없다.")
    void notChangeEmptyTableTest() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        doThrow(IsEmptyTableException.class).when(savedOrderTable).changeNumberOfGuests(any());
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                1L, new ChangeGuestsRequest(1))
        ).isInstanceOf(IsEmptyTableException.class);
    }
}
