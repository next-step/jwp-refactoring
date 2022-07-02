package kitchenpos.service.tablegroup;

import kitchenpos.service.table.dto.OrderTableRequest;
import kitchenpos.service.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupRequestTest {
    @Test
    @DisplayName("단체 지정에 속한 주문 테이블 수가 2 미만이면 실패한다.")
    void create_failed() {
        assertThatThrownBy(() -> new TableGroupRequest(Arrays.asList(new OrderTableRequest(1L)))).isExactlyInstanceOf(
                IllegalArgumentException.class);
        assertThatThrownBy(() -> new TableGroupRequest(Collections.emptyList())).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

}
