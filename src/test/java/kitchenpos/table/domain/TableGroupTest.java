package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TableGroupTest {

    @Test
    @DisplayName("TableGroup의 정상 생성을 확인한다.")
    void createTableGroup() {
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(10, false),
            new OrderTable(10, false));
        TableGroup tableGroup = new TableGroup(orderTables);

        assertThat(tableGroup.getCreatedDate()).isNotNull();
        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @ParameterizedTest(name = "{0}")
    @DisplayName("TableGroup 생성시 실페 케이스를 체크한다.")
    @MethodSource("providerCreateTableGroupFailCase")
    void createMenuFail(String testName, List<OrderTable> orderTables) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new TableGroup(orderTables));
    }

    private static Stream<Arguments> providerCreateTableGroupFailCase() {
        return Stream.of(
            Arguments.of("테이블이 존재하지 않을 경우", Collections.emptyList()),
            Arguments.of("테이블이 한개 일 경우", Collections.singletonList(new OrderTable(10, false)))
        );
    }

}
