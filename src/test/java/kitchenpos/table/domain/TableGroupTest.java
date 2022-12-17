package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객이_존재하는_테이블;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("단체 지정 테스트")
public class TableGroupTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        assertThatNoException().isThrownBy(() -> 단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(
                두_명의_방문객이_존재하는_테이블(),
                두_명의_방문객이_존재하는_테이블()))));
    }

    public static TableGroup 단체_지정(LocalDateTime createdDate, OrderTableBag orderTableBag) {
        return TableGroup.of(createdDate, orderTableBag);
    }
}
