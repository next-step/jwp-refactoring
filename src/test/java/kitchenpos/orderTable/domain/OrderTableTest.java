package kitchenpos.orderTable.domain;

import kitchenpos.exception.IllegalOrderTableException;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.utils.fixture.OrderTableFixtureFactory;
import kitchenpos.utils.fixture.TableGroupFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        orderTable.changeEmpty(true, new ArrayList<>());

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블이 테이블그룹에 등록되어있으면 주문테이블의 비어있음여부를 업데이트할 수 없다")
    @Test
    void OrderTable_Empty_업데이트_TableGroup_검증(){
        TableGroup tableGroup = createTableGroup(LocalDateTime.now(),
                Arrays.asList(createOrderTable(0, true),
                        createOrderTable(0, true)));
        OrderTable orderTable = createOrderTable(tableGroup, 4, false);

        assertThrows(IllegalOrderTableException.class, () -> orderTable.changeEmpty(true, new ArrayList<>()));
    }
}
