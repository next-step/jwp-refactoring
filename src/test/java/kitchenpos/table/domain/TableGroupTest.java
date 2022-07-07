package kitchenpos.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static kitchenpos.factory.TableGroupFixture.테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {

    TableGroup 단체;

    @Test
    @DisplayName("단체지정에서 테이블을 해제한다.")
    void unGroup() {
        //given
        ArrayList<OrderTable> orderTables = new ArrayList<>();
        단체 = new TableGroup(1L, LocalDateTime.now(), orderTables);
        OrderTable 일번테이블 = 테이블_생성(1L, 단체.getId(), 2, false);
        OrderTable 이번테이블 = 테이블_생성(2L, 단체.getId(), 2, false);
        orderTables.add(일번테이블);
        orderTables.add(이번테이블);

        //when
        단체.unGroup();

        //then
        assertAll(() -> {
            assertThat(단체.getOrderTables().getOrderTables().size()).isZero();
            assertThat(일번테이블.getTableGroupId()).isNull();
            assertThat(이번테이블.getTableGroupId()).isNull();
        });
    }
}