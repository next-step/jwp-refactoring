package kitchenpos.table.domain;

import kitchenpos.common.ErrorMessage;
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

@DisplayName("주문 테이블 목록 단위 테스트")
public class OrderTablesTest {
    private TableGroup 우아한_단체_테이블;
    private TableGroup 우테캠프로_단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTable 단체_주문_테이블2;
    private OrderTable 단체_주문_테이블3;
    private OrderTable 단체_주문_테이블4;

    @BeforeEach
    void setUp() {
        우아한_단체_테이블 = new TableGroup();
        우테캠프로_단체_테이블 = new TableGroup();

        단체_주문_테이블1 = new OrderTable(4, true);
        단체_주문_테이블2 = new OrderTable(3, true);
        단체_주문_테이블3 = new OrderTable(3, true);
        단체_주문_테이블4 = new OrderTable(3, true);

        ReflectionTestUtils.setField(우아한_단체_테이블, "id", 1L);
        ReflectionTestUtils.setField(우테캠프로_단체_테이블, "id", 2L);
        ReflectionTestUtils.setField(단체_주문_테이블1, "id", 1L);
        ReflectionTestUtils.setField(단체_주문_테이블2, "id", 2L);
        ReflectionTestUtils.setField(단체_주문_테이블3, "id", 3L);
        ReflectionTestUtils.setField(단체_주문_테이블4, "id", 4L);

        우테캠프로_단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
    }

    @DisplayName("주문 테이블 목록이 비어있으면 테이블 그룹을 생성할 수 없다.")
    @Test
    void 주문_테이블_목록이_비어있으면_테이블_그룹을_생성할_수_없다() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_테이블_목록.group(우아한_단체_테이블, Collections.emptyList()))
                .withMessage(ErrorMessage.TABLE_GROUP_ORDER_TABLES_CANNOT_BE_EMPTY.getMessage());
    }

    @DisplayName("주문 테이블 목록의 크기가 2보다 작으면 테이블 그룹을 생성할 수 없다.")
    @Test
    void 주문_테이블_목록의_크기가_2보다_작으면_테이블_그룹을_생성할_수_없다() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_테이블_목록.group(우아한_단체_테이블, Arrays.asList(단체_주문_테이블3)))
                .withMessage(ErrorMessage.TABLE_GROUP_MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }

    @DisplayName("다른 테이블 그룹에 포함되어 있으면 테이블 그룹을 생성할 수 없다.")
    @Test
    void 다른_테이블_그룹에_포함되어_있으면_테이블_그룹을_생성할_수_없다() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        ReflectionTestUtils.setField(단체_주문_테이블1, "empty", new OrderEmpty(true));
        ReflectionTestUtils.setField(단체_주문_테이블2, "empty", new OrderEmpty(true));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_테이블_목록.group(우아한_단체_테이블, Arrays.asList(단체_주문_테이블1, 단체_주문_테이블3)))
                .withMessage(ErrorMessage.ORDER_TABLE_ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹을_생성한다() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.group(우아한_단체_테이블, Arrays.asList(단체_주문_테이블3, 단체_주문_테이블4));

        assertAll(() -> {
            assertThat(우아한_단체_테이블.getOrderTables()).hasSize(2);
            assertTrue(우아한_단체_테이블.getOrderTables().stream().noneMatch(OrderTable::isEmpty));
        });
    }

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void 주문_테이블을_추가한다() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(우아한_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(우아한_단체_테이블, 단체_주문_테이블4);

        assertThat(우아한_단체_테이블.getOrderTables()).hasSize(2);
    }

    @DisplayName("이미 등록된 주문 테이블은 추가되지 않는다.")
    @Test
    void 이미_등록된_주문_테이블은_추가되지_않는다() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(우아한_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(우아한_단체_테이블, 단체_주문_테이블3);

        assertThat(우아한_단체_테이블.getOrderTables()).hasSize(1);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹을_해제한다() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(우아한_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(우아한_단체_테이블, 단체_주문_테이블4);

        주문_테이블_목록.ungroup();

        assertAll(() -> {
            assertThat(단체_주문_테이블3.getTableGroup()).isNull();
            assertThat(단체_주문_테이블4.getTableGroup()).isNull();
        });
    }
}
