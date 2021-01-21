package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTablesTest {
    private OrderTable orderTable1;
    private OrderTables orderTables = new OrderTables();

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, 4, true);
        orderTables.addTable(orderTable1);
    }

    @Test
    @DisplayName("그룹핑시 최소 테이블 수 체크: 예외처리")
    void tableNumberCheck() {
        assertThatThrownBy(() -> {
            orderTables.checkOrderTables();
        }).isInstanceOf(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderTables.checkOrderTables());
        assertThat(exception.getMessage()).isEqualTo("그룹핑할 테이블이 부족합니다.");
    }

    @Test
    @DisplayName("테이블 상태 체크: 예외처리")
    void checkTableStatus() {
        orderTables.addTable(new OrderTable(2L, 4, false));
        assertThatThrownBy(() -> {
            orderTables.checkOrderTables();
        }).isInstanceOf(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderTables.checkOrderTables());
        assertThat(exception.getMessage()).isEqualTo("테이블이 사용중이거나 이미 그룹핑되어 있습니다.");
    }
}
