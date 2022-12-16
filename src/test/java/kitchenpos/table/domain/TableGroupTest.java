package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객이_존재하는_테이블;
import static kitchenpos.order.domain.OrderTableTest.빈_테이블;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
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

    @DisplayName("생성 예외 - 주문 테이블 목록이 빈 경우")
    @Test
    void 생성_예외_주문_테이블_목록이_빈_경우() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> 단체_지정(LocalDateTime.now(), OrderTableBag.from(Collections.emptyList())));
    }

    @DisplayName("생성 예외 - 빈 상태인 주문 테이블이 포함되어 있을 경우")
    @Test
    void 생성_예외_비어_있지_않은_주문_테이블이_포함되어_있을_경우() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> 단체_지정(LocalDateTime.now(), OrderTableBag.from(Arrays.asList(
                        빈_테이블(),
                        빈_테이블()))));
    }

    public static TableGroup 단체_지정(LocalDateTime createdDate, OrderTableBag orderTableBag) {
        return TableGroup.of(createdDate, orderTableBag);
    }
}
