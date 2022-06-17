package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroup 클래스 테스트")
class TableGroupEntityTest {

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        TableGroupEntity tableGroup = new TableGroupEntity();
        assertThat(tableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("단체 지정에 테이블들을 추가한다.")
    @Test
    void addOrderTables() {
        TableGroupEntity tableGroup = new TableGroupEntity();

        tableGroup.addOrderTables(Arrays.asList(new OrderTableEntity(0, true),
                                                new OrderTableEntity(0, true),
                                                new OrderTableEntity(0, true)));

        assertThat(tableGroup.getOrderTables()).hasSize(3);
    }

    @DisplayName("1개의 테이블을 단체 지정에 추가한다.")
    @Test
    void addOrderTablesWithSizeIsOne() {
        TableGroupEntity tableGroup = new TableGroupEntity();

        assertThatThrownBy(() -> {
            tableGroup.addOrderTables(Arrays.asList(new OrderTableEntity(0, true)));
        }).isInstanceOf(InvalidOrderTablesException.class)
        .hasMessageContaining("테이블 갯수가 적습니다.");
    }

    @DisplayName("빈 테이블이 포함된 테이블들을 단체 지정에 추가한다.")
    @Test
    void addOrderTablesWithHasNotEmpty() {
        TableGroupEntity tableGroup = new TableGroupEntity();

        assertThatThrownBy(() -> {
            tableGroup.addOrderTables(Arrays.asList(new OrderTableEntity(0, false),
                                                    new OrderTableEntity(0, true)));
        }).isInstanceOf(InvalidOrderTablesException.class)
        .hasMessageContaining("테이블 중에 빈 테이블이 아닌 테이블이 있습니다.");
    }

    @DisplayName("다른 단체 지정된 테이블이 포함된 테이블들을 단체 지정에 추가한다.")
    @Test
    void addOrderTablesWithHasGroup() {
        TableGroupEntity tableGroup = new TableGroupEntity();
        TableGroupEntity other = new TableGroupEntity();
        OrderTableEntity orderTable = new OrderTableEntity(0, true);
        orderTable.bindTo(other);

        assertThatThrownBy(() -> {
            tableGroup.addOrderTables(Arrays.asList(orderTable,
                                                    new OrderTableEntity(0, true)));
        }).isInstanceOf(InvalidOrderTablesException.class)
        .hasMessageContaining("테이블 중에 다른 단체 지정된 테이블이 있습니다.");
    }
}