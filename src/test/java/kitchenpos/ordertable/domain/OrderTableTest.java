package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.utils.fixture.OrderTableFixtureFactory.*;
import static kitchenpos.utils.fixture.TableGroupFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("테이블 도메인 테스트")
public class OrderTableTest {
    @DisplayName("주문테이블을 생성할 수 있다")
    @Test
    void OrderTable_생성(){
        OrderTable orderTable = createOrderTable(4, false);
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(4),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블의 비어있음여부를 업데이트할 수 있다")
    @Test
    void OrderTable_Empty_업데이트(){
        OrderTable orderTable = createOrderTable(4, false);
        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블이 테이블그룹에 등록되어있으면 주문테이블의 비어있음여부를 업데이트할 수 없다")
    @Test
    void OrderTable_Empty_업데이트_TableGroup_검증(){
        TableGroup tableGroup = createTableGroup(1L,
                Arrays.asList(createOrderTable(0, true),
                        createOrderTable(0, true)));
        OrderTable orderTable = createOrderTable(tableGroup.getId(), 4, false);

        assertThrows(IllegalOrderTableException.class, () -> orderTable.changeEmpty(true));
    }

    @DisplayName("주문테이블의 손님수를 업데이트할 수 있다")
    @Test
    void OrderTable_손님수_업데이트(){
        OrderTable orderTable = createOrderTable(4, false);
        orderTable.changeNumberOfGuests(5);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("주문테이블의 손님수를 업데이트시 0 이상이어야 한다")
    @Test
    void OrderTable_손님수_업데이트_0이상_검증(){
        OrderTable orderTable = createOrderTable(4, false);

        assertThrows(IllegalOrderTableException.class, () -> orderTable.changeNumberOfGuests(-1));
    }

    @DisplayName("주문테이블의 손님수를 업데이트시 테이블이 비어있을 수 없다")
    @Test
    void OrderTable_손님수_업데이트_Empty_검증(){
        OrderTable orderTable = createOrderTable(0, true);

        assertThrows(IllegalOrderTableException.class, () -> orderTable.changeNumberOfGuests(5));
    }
}
