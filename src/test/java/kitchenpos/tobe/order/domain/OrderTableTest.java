package kitchenpos.tobe.order.domain;

import kitchenpos.tobe.table.domain.TableGroup;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTableTest {

    @Test
    void create() {
        TableGroup tableGroup = new TableGroup(1L, new ArrayList<>(), LocalDateTime.now());
        OrderTable orderTable = new OrderTable(1L, tableGroup, 5, false);

        assertThat(orderTable).isNotNull();
    }
}
