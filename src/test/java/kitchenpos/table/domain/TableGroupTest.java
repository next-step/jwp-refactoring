package kitchenpos.table.domain;

import static common.OrderTableFixture.단체지정_두번째_계산완료;
import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_계산완료;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    void 단체그룹_취소_예외() {
        // given
        TableGroup tableGroup = TableGroup.of(asList(단체지정_첫번째_주문테이블(), 단체지정_두번째_주문테이블()));

        // then
        Assertions.assertThatThrownBy(() -> {
            tableGroup.unGroup();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체그룹_취소() {
        // given
        TableGroup tableGroup = TableGroup.of(asList(단체지정_첫번째_계산완료(), 단체지정_두번째_계산완료()));

        // when
        tableGroup.unGroup();

        List<TableGroup> collect = tableGroup.getOrderTables().stream()
            .map(OrderTable::getTableGroup)
            .collect(Collectors.toList());

        // then
        Assertions.assertThat(collect).containsExactly(null, null);
    }

}
