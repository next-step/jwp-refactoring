package kitchenpos.table.application;

import kitchenpos.common.exception.GuestsNumberOverException;
import kitchenpos.common.exception.NotFoundOrderTableException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.NumberOfGuests;
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
import java.util.Optional;

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
    void createEmptyTable() {
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
    void orderTableList() {
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
    void changeEmpty(boolean empty) {
        // given
        ChangeEmptyRequest 요청_데이터 = new ChangeEmptyRequest(empty);

        OrderTable savedOrderTable = mock(OrderTable.class);
        given(savedOrderTable.isEmpty()).willReturn(empty);
        given(savedOrderTable.getTableGroup()).willReturn(null);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        given(orderRepository.existsByOrderTableAndOrderStatusIn(any(), anyList())).willReturn(false);
        // when
        OrderTableResponse actual = tableService.changeEmpty(anyLong(), 요청_데이터);
        // then
        assertThat(actual.isEmpty()).isEqualTo(empty);
    }

    @Test
    @DisplayName("등록되지 않은 주문 테이블은 변경 할 수 없다.")
    void notFoundOrderTableToChangeEmpty() {
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
    void nonNullTableGroupId() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(savedOrderTable.isNotNullTableGroup()).willReturn(true);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, any()))
                .isInstanceOf(IllegalArgumentException.class);
        verify(orderRepository, never()).existsByOrderTableAndOrderStatusIn(any(), anyList());
    }

    @Test
    @DisplayName("주문 테이블이 존재하고, 주문 상태가 `조리(COOKING)`, `식사(MEAL)`상태이면 변경 할 수 없다.")
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        given(orderRepository.existsByOrderTableAndOrderStatusIn(any(), anyList())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, any()))
                .isInstanceOf(IllegalArgumentException.class);
        verify(orderRepository, only()).existsByOrderTableAndOrderStatusIn(any(), anyList());
    }

    @Test
    @DisplayName("방문한 인원 변경")
    void changeNumberOfGuests() {
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
    void guestsIsOverZero() {
        // given
        ChangeGuestsRequest changeGuestsRequest = new ChangeGuestsRequest(-1);
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeGuestsRequest))
                .isInstanceOf(GuestsNumberOverException.class);
    }

    @Test
    @DisplayName("등록되지 않은 주문 테이블은 변경 할 수 없다.")
    void notFoundOrderTableToChangeNumberOfGuests() {
        // given
        given(orderTableRepository.findByIdElseThrow(anyLong())).willThrow(NotFoundOrderTableException.class);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, any()))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("빈 테이블의 인원은 변경 할 수 없다.")
    void notChangeEmptyTable() {
        // given
        OrderTable savedOrderTable = mock(OrderTable.class);
        given(orderTableRepository.findByIdElseThrow(anyLong())).willReturn(savedOrderTable);
        given(savedOrderTable.isEmpty()).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, any()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
