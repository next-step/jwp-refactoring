package kichenpos.table.table.application;

import static kichenpos.table.table.sample.OrderTableSample.빈_두명_테이블;
import static kichenpos.table.table.sample.OrderTableSample.빈_세명_테이블;
import static kichenpos.table.table.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kichenpos.table.table.domain.Headcount;
import kichenpos.table.table.domain.OrderTable;
import kichenpos.table.table.domain.TableCommandService;
import kichenpos.table.table.domain.TableQueryService;
import kichenpos.table.table.ui.request.EmptyRequest;
import kichenpos.table.table.ui.request.OrderTableRequest;
import kichenpos.table.table.ui.request.TableGuestsCountRequest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest {

    @Mock
    private TableCommandService commandService;
    @Mock
    private TableQueryService queryService;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    void create() {
        //given
        OrderTableRequest request = new OrderTableRequest(3, true);
        OrderTable 빈_세명_테이블 = 빈_세명_테이블();
        when(commandService.save(any())).thenReturn(빈_세명_테이블);

        //when
        tableService.create(request);

        //then
        ArgumentCaptor<OrderTable> orderTableCaptor = ArgumentCaptor.forClass(OrderTable.class);
        verify(commandService, only()).save(orderTableCaptor.capture());
        assertThat(orderTableCaptor.getValue())
            .extracting(OrderTable::hasTableGroup, OrderTable::isEmpty, OrderTable::numberOfGuests)
            .containsExactly(false, request.isEmpty(), Headcount.from(request.getNumberOfGuests()));
    }

    @Test
    @DisplayName("테이블들을 조회할 수 있다.")
    void list() {
        //when
        tableService.list();

        //then
        verify(queryService, only()).findAll();
    }

    @Test
    @DisplayName("테이블의 빈 상태를 변경할 수 있다.")
    void changeEmpty() {
        //given
        long tableId = 1L;
        EmptyRequest request = new EmptyRequest(true);

        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        when(commandService.changeEmpty(anyLong(), anyBoolean())).thenReturn(빈_두명_테이블);

        //when
        tableService.changeEmpty(tableId, request);

        //then
        verify(commandService, only()).changeEmpty(tableId, request.isEmpty());
    }

    @Test
    @DisplayName("방문한 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        //given
        long tableId = 1L;
        TableGuestsCountRequest request = new TableGuestsCountRequest(5);

        OrderTable 채워진_다섯명_테이블 = 채워진_다섯명_테이블();
        when(commandService.changeNumberOfGuests(anyLong(), any(Headcount.class)))
            .thenReturn(채워진_다섯명_테이블);

        //when
        tableService.changeNumberOfGuests(tableId, request);

        //then
        verify(commandService, only()).changeNumberOfGuests(tableId, Headcount.from(5));
    }

    @Test
    @DisplayName("변경하려는 손님 수는 0이상 이어야 한다.")
    void changeNumberOfGuests_lessThanZero_thrownException() {
        //given
        TableGuestsCountRequest request = new TableGuestsCountRequest(-1);

        //when
        ThrowingCallable changeCallable = () -> tableService.changeNumberOfGuests(1L, request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(changeCallable)
            .withMessageEndingWith("이상 이어야 합니다.");
    }

    @Test
    @DisplayName("주문 받은 상태 변경될 수 있다.")
    void changeOrdered() {
        //given
        long orderTableId = 1L;

        //when
        tableService.changeOrdered(orderTableId);

        //then
        verify(commandService, only()).changeOrdered(orderTableId);
    }

    @Test
    @DisplayName("완료된 상태 변경될 수 있다.")
    void changeFinish() {
        //given
        long orderTableId = 1L;

        //when
        tableService.changeFinish(orderTableId);

        //then
        verify(commandService, only()).changeFinish(orderTableId);
    }
}
