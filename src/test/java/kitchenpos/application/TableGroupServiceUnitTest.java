package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.common.Fixtures.aTableGroup;
import static kitchenpos.common.Fixtures.anEmptyOrderTable;
import static kitchenpos.common.Fixtures.tableGroupRequest;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceUnitTest {

    @Mock
    private TableService tableService;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("비어 있지 않은 주문 테이블을 포함하여 그룹화한다")
    @Test
    void testCreateTableGroup_withNotEmptyOrderTable() {
        // given
        TableGroupRequest tableGroupRequest = tableGroupRequest(Arrays.asList(1L, 2L));
        OrderTable orderTable = anEmptyOrderTable()
                .empty(false)
                .build();

        given(tableService.findOrderTableById(any())).willReturn(orderTable);

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("이미 그룹화된 주문 테이블을 포함하여 그룹화한다")
    @Test
    void testCreateTableGroup_withAlreadyGrouped() {
        // given
        TableGroupRequest tableGroupRequest = tableGroupRequest(Arrays.asList(1L, 2L));
        OrderTable orderTable = anEmptyOrderTable().build();
        orderTable.grouping(aTableGroup());

        given(tableService.findOrderTableById(any())).willReturn(orderTable);

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }
}
