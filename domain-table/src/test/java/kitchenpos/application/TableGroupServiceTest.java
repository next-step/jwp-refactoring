package kitchenpos.application;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    TableService tableService;

    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    @Test
    void 단체_지정() {
        // given
        OrderTable firstOrderTable = OrderTable.of(2, true);
        OrderTable secondOrderTable = OrderTable.of(3, true);
        List<OrderTable> orderTables = Arrays.asList(
                firstOrderTable,
                secondOrderTable);
        BDDMockito.given(tableGroupRepository.save(ArgumentMatchers.any())).willReturn(TableGroup.from());
        BDDMockito.given(tableService.findOrderTables((TableGroupRequest) ArgumentMatchers.any())).willReturn(OrderTables.from(orderTables));

        // when
        TableGroupResponse actual = tableGroupService.create(ArgumentMatchers.any());

        // then
        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable firstOrderTable = OrderTable.of(2, true);
        OrderTable secondOrderTable = OrderTable.of(3, true);
        List<OrderTable> orderTables = Arrays.asList(
                firstOrderTable,
                secondOrderTable);

        BDDMockito.given(tableGroupRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(TableGroup.from()));
        BDDMockito.given(tableService.findOrderTables((Long) ArgumentMatchers.any())).willReturn(OrderTables.from(orderTables));

        // when
        tableGroupService.ungroup(ArgumentMatchers.any());

        Assertions.assertThat(orderTables)
                .extracting("tableGroupId")
                .containsExactly(null, null);
    }
}
