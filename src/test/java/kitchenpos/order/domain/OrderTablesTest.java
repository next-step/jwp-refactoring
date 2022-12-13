package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTablesTest {

    private List<OrderTable> orderTableList;

    @BeforeEach
    void setUp() {
        orderTableList = Arrays.asList(
                OrderTable.of(1L, null, 4, false),
                OrderTable.of(2L, null, 4, false));
    }

    @Test
    @DisplayName("주문 테이블 컬렉션을 생성할 때, 주문 테이블이 2개이상이면  생성됨")
    void of4() {
        assertThat(OrderTables.of(orderTableList).getTables().size()).isEqualTo(2);
    }

    @Test
    void unGroup() {
        TableGroup tableGroup = TableGroup.of();
        tableGroup.addOrderTable(orderTableList);
    }
}