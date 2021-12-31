package kitchenpos.table.application;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("테이블 그룹 통합테스트")
public class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹 관리")
    public void tableGroupManage() {
        // given
        // 테이블 그룹 생성
        TableGroupRequest 테이블그룹 = new TableGroupRequest(Arrays.asList(1L, 2L));
        // when
        // 테이블 그룹을 등록한다.
        TableGroupResponse savedTableGroup = tableGroupService.create(테이블그룹);
        // then
        // 테이블 그룹이 정상적으로 등록된다.
        List<OrderTableResponse> orderTables = savedTableGroup.getOrderTables();
        for (OrderTableResponse orderTable : orderTables) {
            assertThat(orderTable.isEmpty()).isFalse();
            assertThat(orderTable.getTableGroupId()).isNotNull();
        }

        Long tableGroupid = savedTableGroup.getId();

        // when
        // 테이블 그룹을 해제한다.
        tableGroupService.ungroup(tableGroupid);

        // then
        // 테이블 그룹이 정상적으로 해제된다.
        assertThat(tableGroupService.countById(tableGroupid)).isEqualTo(0);
    }
}
