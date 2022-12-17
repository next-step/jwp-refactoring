package kitchenpos.table.unit;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("주문테이블기능")
public class OrderTableTest {

    @Test
    @DisplayName("테이블 그룹 존재여부 확인 기능")
    void orderTableTest1() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        OrderTable orderTable = new OrderTable(tableGroup, 0, false);
        assertThatThrownBy(() -> orderTable.validateExist()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비어있는지 확인 기능")
    void priceTest2() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable = new OrderTable(tableGroup, 0, true);
        assertThatThrownBy(() -> orderTable.validateEmpty()).isInstanceOf(IllegalArgumentException.class);
    }

}
