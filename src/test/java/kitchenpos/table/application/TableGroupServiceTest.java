package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
        given(tableService.findOrderTables(any())).willReturn(OrderTables.from(orderTables));
        given(tableGroupRepository.save(any())).willReturn(TableGroup.from(OrderTables.from(orderTables)));

        // when
        TableGroupResponse actual = tableGroupService.create(any());

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

        given(tableGroupRepository.findById(any())).willReturn(Optional.of(TableGroup.from(OrderTables.from(orderTables))));

        // when
        tableGroupService.ungroup(any());

        Assertions.assertThat(orderTables)
                .extracting("tableGroup")
                .containsExactly(null, null);
    }
}
