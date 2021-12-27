package kitchenpos.tablegroup;

import kitchenpos.AcceptanceTest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.global.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체 지정 관련 기능")
class TableGroupServiceTest extends AcceptanceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void createTableGroupFailBecauseOfNotExistTable() {
        // when
        assertThatThrownBy(() -> {
            tableGroupService.create(new TableGroupCreateRequest(Arrays.asList(new TableGroupCreateRequest.OrderTable(1L), new TableGroupCreateRequest.OrderTable(2L))));
        }).isInstanceOf(EntityNotFoundException.class);
    }
}
