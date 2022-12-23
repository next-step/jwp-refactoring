package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static kitchenpos.table.domain.OrderTableFixture.주문테이블;
import static kitchenpos.table.domain.TableGroupFixture.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("TableGroup 클래스 테스트")
public class TableGroupTest {

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;

    private TableGroup 테이블그룹;

    @BeforeEach
    void setup() {
        테이블1 = 주문테이블(1L, null, 0, true);
        테이블2 = 주문테이블(2L, null, 0, true);
        테이블3 = 주문테이블(3L, null, 0, true);

        테이블그룹 = 테이블그룹(1L);
    }

    @DisplayName("주문 테이블을 그룹으로 등록한다")
    @Test
    void 주문_테이블_그룹_테스트() {
        // when
        테이블그룹.group(Arrays.asList(테이블1, 테이블2, 테이블3));

        // then
        assertAll(
                () -> assertThat(테이블그룹.getOrderTables()).hasSize(3),
                () -> assertThat(테이블그룹.getOrderTables().stream()
                        .map(OrderTable::getTableGroup)
                        .collect(Collectors.toList())).containsOnly(테이블그룹),
                () -> assertThat(테이블그룹.getOrderTables().stream()
                        .map(OrderTable::isEmpty)
                        .collect(Collectors.toList())).containsOnly(false)
        );
    }

    @DisplayName("2개 미만의 주문 테이블을 그룹으로 등록한다")
    @Test
    void 두_개_미만_주문_테이블_그룹_테스트() {
        // when & then
        assertThatThrownBy(
                () -> 테이블그룹.group(Arrays.asList(테이블1))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹이 이미 등록된 주문 테이블을 그룹으로 등록한다")
    @Test
    void 그룹으로_등록된_주문_테이블_그룹_테스트() {
        //given
        TableGroup 기존_테이블_그룹 = 테이블그룹(2L);
        OrderTable 테이블4 = 주문테이블(4L, 기존_테이블_그룹, 0, true);

        // when & then
        assertThatThrownBy(
                () -> 테이블그룹.group(Arrays.asList(테이블1, 테이블4))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 주문 테이블을 그룹으로 등록한다")
    @Test
    void 비어있지_않은_테이블_그룹_등록_테스트() {
        //given
        OrderTable 테이블4 = 주문테이블(4L, null, 0, false);

        // when & then
        assertThatThrownBy(
                () -> 테이블그룹.group(Arrays.asList(테이블1, 테이블4))
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
