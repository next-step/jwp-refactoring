package kichenpos.table.group.application;

import static kichenpos.table.group.sample.TableGroupSample.두명_세명_테이블_그룹;
import static kichenpos.table.table.sample.OrderTableSample.빈_두명_테이블;
import static kichenpos.table.table.sample.OrderTableSample.빈_세명_테이블;
import static kichenpos.table.table.sample.OrderTableSample.채워진_다섯명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import kichenpos.common.exception.NotFoundException;
import kichenpos.table.group.domain.TableGroup;
import kichenpos.table.group.domain.TableGroupCommandService;
import kichenpos.table.group.ui.request.TableGroupRequest;
import kichenpos.table.group.ui.request.TableGroupRequest.OrderTableIdRequest;
import kichenpos.table.table.domain.OrderTable;
import kichenpos.table.table.domain.TableQueryService;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 그룹 서비스")
@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Mock
    private TableGroupCommandService groupCommandService;
    @Mock
    private TableQueryService tableQueryService;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("단체 지정 할 수 있다.")
    void create() {
        //given
        long firstOrderTableId = 1L;
        long secondOrderTableId = 2L;
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(
            new OrderTableIdRequest(firstOrderTableId),
            new OrderTableIdRequest(secondOrderTableId)));

        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        when(tableQueryService.table(firstOrderTableId)).thenReturn(빈_두명_테이블);

        OrderTable 빈_세명_테이블 = 빈_세명_테이블();
        when(tableQueryService.table(secondOrderTableId)).thenReturn(빈_세명_테이블);

        TableGroup 두명_세명_테이블_그룹 = 두명_세명_테이블_그룹();
        when(groupCommandService.save(any())).thenReturn(두명_세명_테이블_그룹);

        //when
        tableGroupService.create(request);

        //then
        requestedTableGroupSave();
    }

    @Test
    @DisplayName("등록하려는 단체의 주문 테이블은 반드시 2개 이상이어야 한다.")
    void create_orderTableSizeLessThanTwo_thrownException() {
        //given
        long orderTableId = 1L;
        TableGroupRequest request = new TableGroupRequest(
            Collections.singletonList(new OrderTableIdRequest(orderTableId)));

        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        when(tableQueryService.table(orderTableId)).thenReturn(빈_두명_테이블);

        //when
        ThrowingCallable createCallable = () -> tableGroupService.create(request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable)
            .withMessageEndingWith("이상 이어야 합니다.");
    }

    @Test
    @DisplayName("등록하려는 주문 테이블과 저장 되어있는 주문 테이블의 갯수는 일치해야 한다.")
    void create_differentOrderTableSize_thrownException() {
        //given
        long firstOrderTableId = 1L;
        long secondOrderTableId = 2L;
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(
            new OrderTableIdRequest(firstOrderTableId),
            new OrderTableIdRequest(secondOrderTableId)));

        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        when(tableQueryService.table(firstOrderTableId)).thenReturn(빈_두명_테이블);
        when(tableQueryService.table(secondOrderTableId))
            .thenThrow(new NotFoundException("no table"));

        //when
        ThrowingCallable createCallable = () -> tableGroupService.create(request);

        //then
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 단체의 주문 테이블이 비어있지 않아야 한다.")
    void create_containNotEmptyOrderTable_thrownException() {
        //given
        long firstOrderTableId = 1L;
        long secondOrderTableId = 2L;
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(
            new OrderTableIdRequest(firstOrderTableId),
            new OrderTableIdRequest(secondOrderTableId)));

        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        when(tableQueryService.table(firstOrderTableId)).thenReturn(빈_두명_테이블);
        OrderTable 채워진_다섯명_테이블 = 채워진_다섯명_테이블();
        when(tableQueryService.table(secondOrderTableId)).thenReturn(채워진_다섯명_테이블);

        //when
        ThrowingCallable createCallable = () -> tableGroupService.create(request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable)
            .withMessageEndingWith("그룹이 없어나 비어있어야 합니다.");
    }

    @Test
    @DisplayName("등록하려는 단체의 주문 테이블이 단체가 지정되어 있지 않아야 한다.")
    void create_hasGroup_thrownException() {
        //given
        long firstOrderTableId = 1L;
        long secondOrderTableId = 2L;
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(
            new OrderTableIdRequest(firstOrderTableId),
            new OrderTableIdRequest(secondOrderTableId)));

        OrderTable 빈_두명_테이블 = 빈_두명_테이블();
        when(tableQueryService.table(firstOrderTableId)).thenReturn(빈_두명_테이블);
        OrderTable 그룹이_지정된_테이블 = 두명_세명_테이블_그룹().orderTables().get(0);
        when(tableQueryService.table(secondOrderTableId))
            .thenReturn(그룹이_지정된_테이블);

        //when
        ThrowingCallable createCallable = () -> tableGroupService.create(request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable)
            .withMessageEndingWith("그룹이 없어나 비어있어야 합니다.");
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        //given
        long tableGroupId = 1L;

        //when
        tableGroupService.ungroup(tableGroupId);

        //then
        verify(groupCommandService, only()).ungroup(tableGroupId);
    }

    private void requestedTableGroupSave() {
        ArgumentCaptor<TableGroup> tableGroupCaptor = ArgumentCaptor.forClass(TableGroup.class);
        verify(groupCommandService, times(1)).save(tableGroupCaptor.capture());
        TableGroup tableGroup = tableGroupCaptor.getValue();
        assertThat(tableGroup.orderTables())
            .extracting(OrderTable::isEmpty, orderTable -> orderTable.tableGroup().id())
            .containsExactly(
                tuple(false, tableGroup.id()),
                tuple(false, tableGroup.id())
            );
    }
}
