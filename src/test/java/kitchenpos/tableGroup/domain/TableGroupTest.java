package kitchenpos.tableGroup.domain;

import static common.OrderTableFixture.단체지정_두번째_계산완료;
import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_계산완료;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static common.TableGroupFixture.TABLE_GROUP_ID_1;
import static java.util.Arrays.asList;

import common.TableGroupFixture;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    void 단체그룹_취소_예외() {
        // given
        TableGroup tableGroup = TableGroup.of(asList(단체지정_첫번째_주문테이블(), 단체지정_두번째_주문테이블()));

        // then
        Assertions.assertThatThrownBy(tableGroup::unGroup)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체그룹_취소() {
        // given
        OrderTable 단체지정_첫번째_계산완료 = 단체지정_첫번째_계산완료();
        단체지정_첫번째_계산완료.group(TABLE_GROUP_ID_1);
        OrderTable 단체지정_두번째_계산완료 = 단체지정_두번째_계산완료();
        단체지정_두번째_계산완료.group(TABLE_GROUP_ID_1);
        TableGroup tableGroup = TableGroup.of(asList(단체지정_첫번째_계산완료, 단체지정_두번째_계산완료));

        // when
        tableGroup.unGroup();

        // then
        Assertions.assertThat(단체지정_첫번째_계산완료.getTableGroupId()).isNull();
        Assertions.assertThat(단체지정_두번째_계산완료.getTableGroupId()).isNull();
    }

}
