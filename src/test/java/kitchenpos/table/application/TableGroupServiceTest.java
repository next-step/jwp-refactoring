package kitchenpos.table.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.CreateTableGroupDto;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.table.dto.TableGroupDto;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @DisplayName("create 실패 - orderTable이 비어있음")
    @Test
    void createFail01() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto();

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupDto));
    }

    @DisplayName("create 실패 - orderTable 개수가 2개 미만")
    @Test
    void createFail02() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Collections.singletonList(new OrderTableDto()));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupDto));
    }

    @DisplayName("create 실패 - orderTable 목록에 중복된 것이 존재")
    @Test
    void createFail03() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Lists.newArrayList(dto(1L, true), dto(1L, true)));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.singletonList(entity(1L, true)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupDto));
    }

    @DisplayName("create 실패 - 저장된 orderTable 이 empty 상태가 아님")
    @Test
    void createFail04() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Lists.newArrayList(dto(1L, true), dto(2L, false)));
        given(orderTableRepository.findAllByIdIn(any()))
            .willReturn(Lists.newArrayList(entity(1L, true), entity(2L, false)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupDto));
    }

    @DisplayName("create 성공")
    @Test
    void createSuccess() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Lists.newArrayList(dto(1L, true), dto(2L, true)));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(
            Lists.newArrayList(entity(1L, true), entity(2L, true)));

        TableGroup savedTableGroup = new TableGroup(Lists.newArrayList(entity(1L, true), entity(2L, true)));

        given(tableGroupRepository.save(any())).willReturn(savedTableGroup);

        // when
        tableGroupService.create(tableGroupDto);

        // then
        verify(tableGroupRepository).save(any());
        verify(orderTableRepository, times(tableGroupDto.getOrderTables().size())).save(any());
    }

    @DisplayName("ungroup 실패 - COOKING, MEAL 상태인 order 존재")
    @Test
    void ungroupFail() {
        // given
        Long targetId = 1L;

        TableGroup tableGroup = new TableGroup(Lists.newArrayList(entity(1L, true)));
        given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Collections.singletonList(1L),
                                                                     Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .willReturn(true);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(targetId));
    }

    @DisplayName("ungroup 성공")
    @Test
    void ungroupSuccess() {
        // given
        Long targetId = 1L;

        TableGroup tableGroup = new TableGroup(Lists.newArrayList(entity(1L, true), entity(2L, true)));
        given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                                             .map(OrderTable::getId)
                                             .collect(toList());

        List<OrderTable> savedOrderTables = tableGroup.getOrderTables();
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                                                                     Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)))
            .willReturn(false);

        // when
        tableGroupService.ungroup(targetId);

        // then
        assertThat(savedOrderTables).hasSize(2)
                                    .allMatch(orderTable -> orderTable.getTableGroup() == null);
    }

    private OrderTable entity(Long id, boolean empty) {
        return new OrderTable(id, null, 0, empty);
    }

    private OrderTableDto dto(Long id, boolean empty) {
        return new OrderTableDto(id, null, 0, empty);
    }
}
