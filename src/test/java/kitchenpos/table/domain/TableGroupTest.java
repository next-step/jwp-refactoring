package kitchenpos.table.domain;

import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("테이블 그룹 단위 테스트")
public class TableGroupTest {
    private TableGroup 우아한_테이블;
    private OrderTable 우아한_주문_테이블_1;
    private OrderTable 우아한_주문_테이블_2;

    @BeforeEach
    void setUp() {
        우아한_테이블 = new TableGroup();
        우아한_주문_테이블_1 = new OrderTable(4, true);
        우아한_주문_테이블_2 = new OrderTable(3, true);

        ReflectionTestUtils.setField(우아한_테이블, "id", 1L);
        ReflectionTestUtils.setField(우아한_주문_테이블_1, "id", 1L);
        ReflectionTestUtils.setField(우아한_주문_테이블_2, "id", 2L);
    }

    @DisplayName("주문 테이블 목록이 비어있으면 단체 테이블을 생성할 수 없다.")
    @Test
    void 주문_테이블_목록이_비어있으면_단체_테이블을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 우아한_테이블.group(Collections.emptyList()))
                .withMessage(ErrorMessage.TABLE_GROUP_ORDER_TABLES_CANNOT_BE_EMPTY.getMessage());
    }

    @DisplayName("주문 테이블 목록의 크기가 2개 보다 작으면 단체 테이블을 생성할 수 없다.")
    @Test
    void 주문_테이블_목록의_크기가_2개_보다_작으면_단체_테이블을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 우아한_테이블.group(Arrays.asList(우아한_주문_테이블_1)))
                .withMessage(ErrorMessage.TABLE_GROUP_MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }

    @DisplayName("다른 단체 테이블에 포함된 주문 테이블이 있으면 단체 테이블을 생성할 수 없다.")
    @Test
    void 다른_단체_테이블에_포함된_주문_테이블이_있으면_단체_테이블을_생성할_수_없다() {
        우아한_테이블.group(Arrays.asList(우아한_주문_테이블_1, 우아한_주문_테이블_2));

        ReflectionTestUtils.setField(우아한_주문_테이블_1, "empty", new OrderEmpty(true));
        ReflectionTestUtils.setField(우아한_주문_테이블_2, "empty", new OrderEmpty(true));

        TableGroup 다른_단체_테이블 = new TableGroup();
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 다른_단체_테이블.group(Arrays.asList(우아한_주문_테이블_1, 우아한_주문_테이블_2)))
                .withMessage(ErrorMessage.ORDER_TABLE_ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @DisplayName("단체 테이블 생성한다.")
    @Test
    void 단체_테이블_생성한다() {
        우아한_테이블.group(Arrays.asList(우아한_주문_테이블_1, 우아한_주문_테이블_2));

        assertAll(() -> {
            assertThat(우아한_테이블.getOrderTables()).hasSize(2);
            assertTrue(우아한_테이블.getOrderTables().stream().noneMatch(OrderTable::isEmpty));
        });
    }

    @DisplayName("단체 테이블 해제한다.")
    @Test
    void 단체_테이블_해제한다() {
        우아한_테이블.group(Arrays.asList(우아한_주문_테이블_1, 우아한_주문_테이블_2));
        우아한_테이블.ungroup();

        assertAll(() -> {
            assertThat(우아한_주문_테이블_1.getTableGroup()).isNull();
            assertThat(우아한_주문_테이블_2.getTableGroup()).isNull();
        });
    }
}
