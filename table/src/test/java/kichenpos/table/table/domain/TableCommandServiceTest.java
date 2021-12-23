package kichenpos.table.table.domain;

import static kichenpos.table.table.sample.OrderTableSample.빈_두명_테이블;
import static kichenpos.table.table.sample.OrderTableSample.빈_세명_테이블;
import static kichenpos.table.table.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kichenpos.common.exception.InvalidStatusException;
import kichenpos.common.exception.NotFoundException;
import kichenpos.table.group.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 커맨드 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class TableCommandServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableCommandService commandService;

    @Test
    @DisplayName("테이블 저장")
    void save() {
        //given
        OrderTable 빈_세명_테이블 = 빈_세명_테이블();

        //when
        commandService.save(빈_세명_테이블);

        //then
        verify(orderTableRepository, only()).save(빈_세명_테이블);
    }

    @Test
    @DisplayName("테이블의 빈 상태를 변경")
    void changeEmpty() {
        //given
        OrderTable 채워진_다섯명_테이블 = 채워진_다섯명_테이블();
        when(orderTableRepository.table(anyLong())).thenReturn(채워진_다섯명_테이블);

        //when
        OrderTable orderTable = commandService.changeEmpty(1L, true);

        //then
        assertThat(orderTable)
            .extracting(OrderTable::isEmpty)
            .isEqualTo(true);
    }

    @Test
    @DisplayName("빈테이블 여부를 변경하려면 변경하려는 테이블의 정보가 반드시 저장되어 있어야 함")
    void changeEmpty_notExistOrderTable_thrownException() {
        //given
        when(orderTableRepository.table(anyLong())).thenThrow(NotFoundException.class);

        //when
        ThrowingCallable changeCallable = () -> commandService.changeEmpty(1L, false);

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("빈테이블 여부를 변경하려면 그룹이 지정되어 있지 않아야 함")
    void changeEmpty_existTableGroupId_thrownException() {
        //given
        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        OrderTable 빈_세명_테이블 = 빈_세명_테이블();
        TableGroup.from(Arrays.asList(빈_두명_테이블, 빈_세명_테이블));
        when(orderTableRepository.table(anyLong())).thenReturn(빈_두명_테이블);

        //when
        ThrowingCallable changeCallable = () -> commandService.changeEmpty(1L, false);

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeCallable)
            .withMessageEndingWith("그룹이 지정되어 있어서 상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("빈테이블 여부를 변경하는데 테이블이 주문된 상태라면 변경이 불가능")
    void changeEmpty_ordered_thrownException() {
        //given
        OrderTable orderTable = 채워진_다섯명_테이블();
        orderTable.ordered();
        when(orderTableRepository.table(anyLong()))
            .thenReturn(orderTable);

        //when
        ThrowingCallable changeCallable = () -> commandService.changeEmpty(1L, false);

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeCallable)
            .withMessageEndingWith("상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("방문한 손님 수를 변경")
    void changeNumberOfGuests() {
        //given
        Headcount updatedNumberOfGuests = Headcount.from(3);

        OrderTable 채워진_다섯명_테이블 = 채워진_다섯명_테이블();
        when(orderTableRepository.table(anyLong()))
            .thenReturn(채워진_다섯명_테이블);

        //when
        OrderTable orderTable = commandService.changeNumberOfGuests(1L, updatedNumberOfGuests);

        //then
        assertThat(orderTable)
            .extracting(OrderTable::numberOfGuests)
            .isEqualTo(updatedNumberOfGuests);
    }

    @Test
    @DisplayName("변경하려는 테이블의 정보가 반드시 저장되어 있어야 함")
    void changeNumberOfGuests_notExistOrderTable_thrownException() {
        //given
        when(orderTableRepository.table(anyLong())).thenThrow(NotFoundException.class);

        //when
        ThrowingCallable changeCallable = () ->
            commandService.changeNumberOfGuests(1L, Headcount.from(3));

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(changeCallable);
    }

    @Test
    @DisplayName("방문한 손님 수를 변경하려는 테이블은 비어있지 않아야 함")
    void changeNumberOfGuests_empty_thrownException() {
        //given
        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        when(orderTableRepository.table(anyLong())).thenReturn(빈_두명_테이블);

        //when
        ThrowingCallable changeCallable = () ->
            commandService.changeNumberOfGuests(1L, Headcount.from(3));

        //then
        assertThatExceptionOfType(InvalidStatusException.class)
            .isThrownBy(changeCallable)
            .withMessageEndingWith("방문한 손님 수를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 받은 상태 변경")
    void changeOrdered() {
        //given
        OrderTable orderTable = mock(OrderTable.class);
        when(orderTableRepository.table(anyLong())).thenReturn(orderTable);

        //when
        commandService.changeOrdered(1L);

        //then
        verify(orderTable, only()).ordered();
    }

    @Test
    @DisplayName("주문 받은 상태 변경")
    void changeFinish() {
        //given
        OrderTable orderTable = mock(OrderTable.class);
        when(orderTableRepository.table(anyLong())).thenReturn(orderTable);

        //when
        commandService.changeFinish(1L);

        //then
        verify(orderTable, only()).finish();
    }
}
