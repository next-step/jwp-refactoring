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
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupMapper;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void create() {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(
            Arrays.asList(new OrderTableIdRequest(), new OrderTableIdRequest()));
        final OrderTable orderTable = new OrderTable(2);
        orderTable.changeEmpty(true);
        final OrderTable orderTable2 = new OrderTable(3);
        orderTable2.changeEmpty(true);
        final List<OrderTable> savedOrderTables = Arrays.asList(orderTable, orderTable2);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(savedOrderTables);
        final TableGroup tableGroup = new TableGroup();
        final TableGroupMapper tableGroupMapper = new TableGroupMapper(tableGroupValidator, tableGroupRequest);
        given(TableGroupMapper.of(tableGroupValidator, tableGroupRequest)).willReturn(tableGroupMapper);
        given(tableGroupMapper.toTableGroup()).willReturn(tableGroup);
        given(tableGroup.getId()).willReturn(1L);

        // when
        tableGroupService.create(tableGroupRequest);

        // then
        verify(tableGroupRepository).save(any(TableGroup.class));
    }

    @ParameterizedTest
    @MethodSource("provideOrderTables")
    void given_InvalidOrderTables_when_Create_then_ThrownException(final List<OrderTableIdRequest> orderTables) {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);

        // when
        final Throwable oneOrderTableException = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        // then
        assertThat(oneOrderTableException).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideOrderTables() {
        return Stream.of(
            Arguments.of((Object)null),
            Arguments.of(Collections.singletonList(new OrderTableIdRequest()))
        );
    }

    @ParameterizedTest
    @MethodSource("provideAllOrderTables")
    void given_InvalidTableGroup_when_Create_then_ThrownException(final List<OrderTable> orderTables) {
        // given
        final TableGroupRequest twoOrderTables = new TableGroupRequest(
            Arrays.asList(new OrderTableIdRequest(), new OrderTableIdRequest()));
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
        given(orderTableRepository.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableRepository).findAllByTableGroupId(tableGroupId);
        verify(orderRepository).findAllByOrderTableIdIn(anyList());
    }
}
