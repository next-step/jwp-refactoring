package kitchenpos.order.application;

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

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void create() {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest();
        tableGroupRequest.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        final OrderTable orderTable = new OrderTable(2);
        orderTable.changeEmpty(true);
        final OrderTable orderTable2 = new OrderTable(3);
        orderTable2.changeEmpty(true);
        final List<OrderTable> savedOrderTables = Arrays.asList(orderTable, orderTable2);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(savedOrderTables);

        // when
        tableGroupService.create(tableGroupRequest);

        // then
        verify(tableGroupRepository).save(any(TableGroup.class));
    }

    @ParameterizedTest
    @MethodSource("provideOrderTables")
    void given_InvalidOrderTables_when_Create_then_ThrownException(final List<OrderTable> orderTables) {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest();
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
    void given_InvalidTableGroup_when_Create_then_ThrownException(final List<OrderTable> orderTables) {
        // given
        final TableGroupRequest twoOrderTables = new TableGroupRequest();
        twoOrderTables.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);

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
        final Long tableGroupId = 1L;
        final OrderTable orderTable = new OrderTable();
        final OrderTable orderTable2 = new OrderTable();
        final List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        given(orderTableRepository.findAllByTableGroup_Id(tableGroupId)).willReturn(orderTables);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableRepository).findAllByTableGroup_Id(tableGroupId);
        verify(orderRepository).findAllByOrderTable_IdIn(anyList());
    }

    @Test
    void given_GroupedOrderTables_when_CreateTableGroup_then_ThrowException() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(new OrderTable(new TableGroup(), 1),
            new OrderTable(new TableGroup(), 2));

        // when
        final Throwable throwable = catchThrowable(() -> new TableGroup(new OrderTables(orderTables)));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
