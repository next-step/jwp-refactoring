package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static kitchenpos.factory.TableGroupFixture.테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TableGroupTest {

    TableGroup 단체;

    @Test
    @DisplayName("단체지정에서 테이블을 해제한다.")
    void unGroup() {
        //given
        OrderTable 일번테이블 = 테이블_생성(1L, 단체, 2, false);
        OrderTable 이번테이블 = 테이블_생성(2L, 단체, 2, false);
        ArrayList<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(일번테이블);
        orderTables.add(이번테이블);
        단체 = new TableGroup(1L, LocalDateTime.now(), orderTables);

        //when
        단체.unGroup();

        //then
        assertAll(() -> {
            assertThat(단체.getOrderTables().getOrderTables().size()).isZero();
            assertThat(일번테이블.getTableGroup()).isNull();
            assertThat(이번테이블.getTableGroup()).isNull();
        });
    }
}