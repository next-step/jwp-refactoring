package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;

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
    void create() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        OrderTable orderTable = new OrderTable(2);
        orderTable.changeEmpty(true);
        OrderTable orderTable2 = new OrderTable(3);
        orderTable2.changeEmpty(true);
        List<OrderTable> savedOrderTables = Arrays.asList(orderTable, orderTable2);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(savedOrderTables);

        // when
        tableGroupService.create(tableGroupRequest);

        // then
        verify(tableGroupDao).save(any(TableGroup.class));
    }

    @ParameterizedTest
    @MethodSource("provideOrderTables")
    void given_InvalidOrderTables_when_Create_then_ThrownException(List<OrderTable> orderTables) {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTables(orderTables);

        // when
        final Throwable oneOrderTableException = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        // then
        assertThat(oneOrderTableException).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideOrderTables() {
        return Stream.of(
            Arguments.of((Object)null),
            Arguments.of(Collections.singletonList(new OrderTable()))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAllOrderTables")
    void given_InvalidTableGroup_when_Create_then_ThrownException(List<OrderTable> orderTables) {
        // given
        TableGroupRequest twoOrderTables = new TableGroupRequest();
        twoOrderTables.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        // when
        final Throwable differentTableSize = catchThrowable(() -> tableGroupService.create(twoOrderTables));

        // then
        assertThat(differentTableSize).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideAllOrderTables() {
        return Stream.of(
            Arguments.of(Arrays.asList(new OrderTable(), new OrderTable())),
            Arguments.of(Collections.singletonList(new OrderTable()))
        );
    }

    @Test
    void ungroup() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        given(orderTableDao.findAllByTableGroup_Id(tableGroupId)).willReturn(orderTables);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableDao).findAllByTableGroup_Id(tableGroupId);
        verify(orderDao).findAllByOrderTable_IdIn(anyList());
    }

    @Test
    void given_GroupedOrderTables_when_CreateTableGroup_then_ThrowException() {
        // given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(new TableGroup(), 1), new OrderTable(new TableGroup(), 2));

        // when
        final Throwable throwable = catchThrowable(() -> new TableGroup(new OrderTables(orderTables)));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
