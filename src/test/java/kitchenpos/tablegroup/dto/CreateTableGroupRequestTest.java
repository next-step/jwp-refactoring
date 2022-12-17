package kitchenpos.tablegroup.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("단체 지정 요청 데이터 테스트")
public class CreateTableGroupRequestTest {

    @Test
    void 생성_성공() {
        //given:
        final long 첫_번째_테이블_id = 1l;
        final long 두_번째_테이블_id = 2l;
        //when, then:
        assertThatNoException().isThrownBy(() -> 단체_지정_요청(Arrays.asList(첫_번째_테이블_id, 두_번째_테이블_id)));
    }

    public static CreateTableGroupRequest 단체_지정_요청(List<Long> orderTableIds) {
        return new CreateTableGroupRequest(orderTableIds);
    }
}
