package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.TableServiceTest.주문_테이블;
import static kitchenpos.domain.OrderTableTest.두_명의_방문객;
import static kitchenpos.domain.OrderTableTest.비어있지_않은_상태;
import static kitchenpos.domain.OrderTableTest.빈_상태;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("단체 지정 테스트")
public class TableGroupTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        assertThatNoException().isThrownBy(() -> 단체_지정(LocalDateTime.now(), Arrays.asList(
                주문_테이블(두_명의_방문객, 빈_상태),
                주문_테이블(두_명의_방문객, 빈_상태))));
    }

    @DisplayName("생성 예외 - 주문 테이블 목록이 빈 경우")
    @Test
    void 생성_예외_주문_테이블_목록이_빈_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> 단체_지정(LocalDateTime.now(), Collections.emptyList()));
    }

    @DisplayName("생성 예외 - 비어 있지 않은 주문 테이블이 포함되어 있을 경우")
    @Test
    void 생성_예외_비어_있지_않은_주문_테이블이_포함되어_있을_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> 단체_지정(LocalDateTime.now(), Arrays.asList(
                주문_테이블(두_명의_방문객, 비어있지_않은_상태),
                주문_테이블(두_명의_방문객, 비어있지_않은_상태))));
    }

    public static TableGroup 단체_지정(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return TableGroup.of(createdDate, orderTables);
    }
}
