package kitchenpos.application;

import static kitchenpos.application.sample.OrderTableSample.빈_세명_테이블;
import static kitchenpos.application.sample.OrderTableSample.빈_두명_테이블;
import static kitchenpos.application.sample.OrderTableSample.채워진_다섯명_테이블;
import static kitchenpos.application.sample.TableGroupSample.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.table.ui.request.TableGroupRequest;
import kitchenpos.table.ui.request.TableGroupRequest.OrderTableIdRequest;
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
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("단체 지정 할 수 있다.")
    void create() {
        //given
        TableGroupRequest request = new TableGroupRequest(
            Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

        when(orderTableDao.findAllByIdIn(anyList()))
            .thenReturn(Arrays.asList(빈_두명_테이블(), 빈_세명_테이블()));

        TableGroup savedTableGroup = tableGroup();
        when(tableGroupDao.save(any())).thenReturn(savedTableGroup);

        //when
        tableGroupService.create(request);

        //then
        assertAll(
            this::requestedTableGroupSave,
            () -> requestedOrderTableSave(savedTableGroup)
        );
    }

    @Test
    @DisplayName("등록하려는 단체의 주문 테이블은 반드시 2개 이상이어야 한다.")
    void create_orderTableSizeLessThanTwo_thrownException() {
        //given
        TableGroupRequest request = new TableGroupRequest(
            Collections.singletonList(new OrderTableIdRequest(1L)));

        //when
        ThrowingCallable createCallable = () -> tableGroupService.create(request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 주문 테이블과 저장 되어있는 주문 테이블의 갯수는 일치해야 한다.")
    void create_differentOrderTableSize_thrownException() {
        //given
        TableGroupRequest request = new TableGroupRequest(
            Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

        when(orderTableDao.findAllByIdIn(anyList()))
            .thenReturn(Collections.singletonList(빈_두명_테이블()));

        //when
        ThrowingCallable createCallable = () -> tableGroupService.create(request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("등록하려는 단체의 주문 테이블이 비어있지 않거나 단체가 지정되어 있으면 안된다.")
    void create_containNotEmptyOrderTable_thrownException() {
        //given
        TableGroupRequest request = new TableGroupRequest(
            Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

        when(orderTableDao.findAllByIdIn(anyList()))
            .thenReturn(Arrays.asList(빈_두명_테이블(), 채워진_다섯명_테이블()));

        //when
        ThrowingCallable createCallable = () -> tableGroupService.create(request);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        //given
        long tableGroupId = 1L;

        OrderTable orderTable = 채워진_다섯명_테이블();
        orderTable.setTableGroupId(tableGroupId);
        when(orderTableDao.findAllByTableGroupId(tableGroupId))
            .thenReturn(Collections.singletonList(orderTable));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(false);

        //when
        tableGroupService.ungroup(tableGroupId);

        //then
        ArgumentCaptor<OrderTable> orderTableCaptor = ArgumentCaptor.forClass(OrderTable.class);
        verify(orderTableDao, times(1)).save(orderTableCaptor.capture());
        assertThat(orderTableCaptor.getValue())
            .extracting(OrderTable::getTableGroupId)
            .isNull();
    }

    @Test
    @DisplayName("해제하려는 단체에 조리 또는 식사 중인 주문이 있으면 해제가 불가능하다.")
    void ungroup_cookOrMealStatus_thrownException() {
        //given
        long tableGroupId = 1L;

        OrderTable orderTable = 채워진_다섯명_테이블();
        orderTable.setTableGroupId(tableGroupId);
        when(orderTableDao.findAllByTableGroupId(tableGroupId))
            .thenReturn(Collections.singletonList(orderTable));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(true);

        //when
        ThrowingCallable createCallable = () -> tableGroupService.ungroup(tableGroupId);

        //then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(createCallable);
    }

    private void requestedOrderTableSave(TableGroup tableGroup) {
        ArgumentCaptor<OrderTable> orderTableCaptor = ArgumentCaptor.forClass(OrderTable.class);
        verify(orderTableDao, times(2)).save(orderTableCaptor.capture());
        assertThat(orderTableCaptor.getAllValues())
            .extracting(OrderTable::isEmpty, OrderTable::getTableGroupId)
            .containsExactly(
                tuple(false, tableGroup.getId()),
                tuple(false, tableGroup.getId())
            );
    }

    private void requestedTableGroupSave() {
        ArgumentCaptor<TableGroup> tableGroupCaptor = ArgumentCaptor.forClass(TableGroup.class);
        verify(tableGroupDao, times(1)).save(tableGroupCaptor.capture());
        assertThat(tableGroupCaptor.getValue().getCreatedDate())
            .isEqualToIgnoringMinutes(LocalDateTime.now());
    }
}
