package kitchenpos.table.domain.repository;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TableGroupRepositoryTest {
    @Autowired
    TableGroupRepository tableGroupRepository;
    @Autowired
    OrderTableRepository orderTableRepository;

    private OrderTable orderTable_1;
    private OrderTable orderTable_2;
    @BeforeEach
    void setUp() {
        orderTable_1 = orderTableRepository.save(OrderTable.of(0, true));
        orderTable_2 = orderTableRepository.save(OrderTable.of(0, true));
    }

    @Test
    @DisplayName("테이블 그룹 생성")
    void create() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(OrderTable.of(0, true), OrderTable.of(0, true));
        final List<OrderTable> savedOrderTabled = orderTableRepository.findAllById(Arrays.asList(orderTable_1.getId(), orderTable_2.getId()));
        final TableGroup tableGroup = TableGroup.of(orderTables, savedOrderTabled.size());
        // when
        final TableGroup group = tableGroupRepository.save(tableGroup);
        // then
        assertThat(group).isInstanceOf(TableGroup.class);
    }
}
