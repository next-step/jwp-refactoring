package kitchenpos.domain;

import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 테스트")
class TableGroupTest {

    OrderTables orderTables;
    OrderTable firstOrderTable;
    OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        firstOrderTable = OrderTableFixture.of(2, true);
        secondOrderTable = OrderTableFixture.of(2, true);
        orderTables = OrderTables.from(Arrays.asList(firstOrderTable, secondOrderTable));
    }

    @Test
    void 단체_지정() {
        // given
        TableGroup actual = TableGroup.from();

        // when
        actual.group(orderTables);

        // then
        assertThat(actual).isNotNull();
    }
}
