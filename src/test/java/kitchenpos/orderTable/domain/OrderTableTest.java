package kitchenpos.orderTable.domain;

import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTableTest {
    @DisplayName("주문테이블을 생성할 수 있다")
    @Test
    void OrderTable_생성(){
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(4),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블의 비어있음여부를 업데이트할 수 있다")
    @Test
    void OrderTable_Empty_업데이트(){
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        orderTable.changeEmpty(true, new ArrayList<>());

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블이 테이블그룹에 등록되어있으면 주문테이블의 비어있음여부를 업데이트할 수 없다")
    @Test
    void OrderTable_Empty_업데이트_TableGroup_검증(){
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(),
                Arrays.asList(new OrderTable(0, true),
                        new OrderTable(0, true)));
        OrderTable orderTable = new OrderTable(1L, tableGroup, 4, false);

        assertThrows(IllegalArgumentException.class, () -> orderTable.changeEmpty(true, new ArrayList<>()));
    }
}
