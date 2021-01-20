package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TableGroupTest {

    private TableGroup tableGroup;
    private OrderTable 빈테이블_1;
    private OrderTable 빈테이블_2;
    private OrderTable 비어있지_않은_테이블_3;
    private TableGroup 그룹_테이블_1;
    private OrderTable 그룹_지정된_테이블_1;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup(1L, LocalDateTime.now());
        빈테이블_1 = new OrderTable(1L, null,0, true);
        빈테이블_2 = new OrderTable(2L, null,0, true);
        비어있지_않은_테이블_3 = new OrderTable(2L, null,0, false);
        그룹_테이블_1 = new TableGroup(1L, LocalDateTime.of(2020, 1, 20, 03, 30));
        그룹_지정된_테이블_1 = new OrderTable(10L, 그룹_테이블_1, 0, false);
    }

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(빈테이블_1, 빈테이블_2));

        // when
        tableGroup.updateOrderTables(orderTables);

        // then
        assertThat(tableGroup).isNotNull();
        assertThat(빈테이블_1.getTableGroupId()).isNotNull();
        assertThat(빈테이블_2.getTableGroupId()).isNotNull();
    }

    @DisplayName("주문 테이블 상태가 비어있음이 아니면 생성할 수 없다.")
    @Test
    void isNotEmpty() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderTables orderTables = new OrderTables(Arrays.asList(빈테이블_1, 비어있지_않은_테이블_3));
            tableGroup.updateOrderTables(orderTables);
        }).withMessageMatching("비어있지 않거나 이미 그룹 지정된 테이블은 그룹 지정할 수 없습니다.");

    }

    @DisplayName("이미 단체 지정이 되어 있으면 지정할 수 없다.")
    @Test
    void alreadyGroup() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            OrderTables orderTables = new OrderTables(Arrays.asList(빈테이블_1, 그룹_지정된_테이블_1));
            tableGroup.updateOrderTables(orderTables);
        }).withMessageMatching("비어있지 않거나 이미 그룹 지정된 테이블은 그룹 지정할 수 없습니다.");
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        tableGroup.updateOrderTables(new OrderTables(Arrays.asList(빈테이블_1, 빈테이블_2)));

        // when
        tableGroup.unGroup();

        // then
        assertThat(빈테이블_1.getTableGroupId()).isNull();
        assertThat(빈테이블_2.getTableGroupId()).isNull();
    }
}
