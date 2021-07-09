package kitchenpos.tobe.table.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableTest {

    @Test
    void create() {
        OrderTable orderTable = new OrderTable(1L, null, 5, false);

        assertThat(orderTable).isNotNull();
    }
}
