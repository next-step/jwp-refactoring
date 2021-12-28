package kitchenpos.tablegroup.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.KitchenposException;

class TableGroupRequestTest {

    @DisplayName("요청 생성 에러")
    @Test
    void construct() {
        // given
        List<OrderTableIdRequest> requestTables = Arrays.asList(new OrderTableIdRequest(2L));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(requestTables);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableGroupRequest.checkValidSize())
            .withMessage("테이블 그룹을 생성하기 위해 2개 이상의 테이블이 필요합니다.");
    }
}