package kitchenpos.application;

import static kitchenpos.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.OrderTables;

@DisplayName("단체 지정 유효성 검사")
@SpringBootTest
class TableGroupValidatorTest {
    @Autowired
    private TableGroupValidator tableGroupValidator;

    @DisplayName("단체 지정 등록 API - 주문 테이블 없음")
    @Test
    void create_empty_order_tables() {
        // given
        List<Long> orderTableIds = new ArrayList<>();
        OrderTables savedOrderTables = new OrderTables(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> tableGroupValidator.validateCreate(orderTableIds, savedOrderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 주문 테이블 2개 미만")
    @Test
    void create_order_tables_less_than_2() {
        // given
        Long orderTableId1 = 1L;
        List<Long> orderTableIds = Collections.singletonList(orderTableId1);
        OrderTables savedOrderTables = new OrderTables(
            Collections.singletonList(savedOrderTable(orderTableId1, true))
        );

        // when, then
        assertThatThrownBy(() -> tableGroupValidator.validateCreate(orderTableIds, savedOrderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 등록되어있지 않는 주문 테이블 존재")
    @Test
    void create_order_tables_not_saved() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        List<Long> orderTableIds = Arrays.asList(orderTableId1, orderTableId2);
        OrderTables savedOrderTables = new OrderTables(
            Collections.singletonList(savedOrderTable(orderTableId1, true))
        );

        // when, then
        assertThatThrownBy(() -> tableGroupValidator.validateCreate(orderTableIds, savedOrderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 비어있지 않은 주문 테이블 존재")
    @Test
    void create_order_table_not_empty() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        List<Long> orderTableIds = Arrays.asList(orderTableId1, orderTableId2);
        OrderTables savedOrderTables = new OrderTables(Arrays.asList(
            savedOrderTable(orderTableId1, true),
            savedOrderTable(orderTableId2, false)
        ));
        // when, then
        assertThatThrownBy(() -> tableGroupValidator.validateCreate(orderTableIds, savedOrderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록 API - 다른 단체 지정에 연결되어있는 주문 테이블 존재")
    @Test
    void create_order_table_table_group_id_exists() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        List<Long> orderTableIds = Arrays.asList(orderTableId1, orderTableId2);
        OrderTables savedOrderTables = new OrderTables(Arrays.asList(
            savedOrderTable(orderTableId1, true),
            savedOrderTable(orderTableId2, 1L, false)
        ));

        // when, then
        assertThatThrownBy(() -> tableGroupValidator.validateCreate(orderTableIds, savedOrderTables))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
