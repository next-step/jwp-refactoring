package kitchenpos.table.application;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.CreateTableGroupDto;
import kitchenpos.table.dto.OrderTableIdDto;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

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
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Collections.singletonList(new OrderTableIdDto()));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupDto));
    }

    @DisplayName("create 실패 - orderTable 목록에 중복된 것이 존재")
    @Test
    void createFail03() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Lists.newArrayList(dto(1L), dto(1L)));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.singletonList(entity(1L, true)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupDto));
    }

    @DisplayName("create 실패 - 저장된 orderTable 이 empty 상태가 아님")
    @Test
    void createFail04() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Lists.newArrayList(dto(1L), dto(2L)));
        given(orderTableRepository.findAllByIdIn(any()))
            .willReturn(Lists.newArrayList(entity(1L, true), entity(2L, false)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupDto));
    }

    @DisplayName("create 성공")
    @Test
    void createSuccess() {
        // given
        CreateTableGroupDto tableGroupDto = new CreateTableGroupDto(Lists.newArrayList(dto(1L), dto(2L)));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(
            Lists.newArrayList(entity(1L, true), entity(2L, true)));

        TableGroup savedTableGroup = new TableGroup(Lists.newArrayList(entity(1L, true), entity(2L, true)));

        given(tableGroupRepository.save(any())).willReturn(savedTableGroup);

        // when
        tableGroupService.create(tableGroupDto);

        // then
        verify(tableGroupRepository).save(any());
    }

    @DisplayName("ungroup 실패 - COOKING, MEAL 상태인 order 존재")
    @Test
    void ungroupFail() {
        // given
        Long targetId = 1L;

        OrderTable orderTable1 = entity(1L, false);
        orderTable1.addOrder(new Order()); // add COOKING order
        orderTable1.empty();

        OrderTable orderTable2 = entity(2L, false);
        orderTable2.addOrder(new Order()); // add COOKING order
        orderTable2.empty();

        TableGroup tableGroup = new TableGroup(Lists.newArrayList(orderTable1, orderTable2));

        given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

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

        // when
        tableGroupService.ungroup(targetId);

        // then
        assertThat(tableGroup.getOrderTables().getData()).isEmpty();
    }

    private OrderTable entity(Long id, boolean empty) {
        return new OrderTable(id, null, 0, empty);
    }

    private OrderTableIdDto dto(Long id) {
        return new OrderTableIdDto(id);
    }
}
