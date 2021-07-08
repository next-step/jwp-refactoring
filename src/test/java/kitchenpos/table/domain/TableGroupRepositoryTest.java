package kitchenpos.table.domain;

import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    private TableGroupRequest tableGroupRequest;

    private Long tableGroup1Id = 1L;
    private OrderTable orderTable1 = new OrderTable(1L, null, 0, true);
    private OrderTable orderTable2 = new OrderTable(2L, null, 0, true);

    @BeforeEach
    void setUp() {
//        tableGroupRequest = TableGroupRequest.of(tableGroup1Id, tableGroup1OrderTables);
    }

    @Test
    void create() {
        OrderTable persistOrderTable1 = orderTableRepository.save(orderTable1);
        OrderTable persistOrderTable2 = orderTableRepository.save(orderTable2);

        TableGroup tableGroup = new TableGroup(Arrays.asList(persistOrderTable1, persistOrderTable2));

        TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);

        assertThat(persistTableGroup.getId()).isEqualTo(tableGroup1Id);
        assertThat(persistTableGroup.getOrderTables()).contains(persistOrderTable1, persistOrderTable2);
    }

}
