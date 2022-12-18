package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable tableA;
    private OrderTable tableB;

    @BeforeEach
    void setUp() {
        tableA = new OrderTable(0, true);
        tableB = new OrderTable(0, true);
        orderTableRepository.saveAll(Arrays.asList(tableA, tableB));
    }

    @DisplayName("테이블 그룹을 추가할 수 있다.")
    @Test
    void create() {
        TableGroupResponse actual = tableGroupService.create(Arrays.asList(tableA.getId(), tableB.getId()));

        List<OrderTable> groupedTables = orderTableRepository.findAllByTableGroupId(actual.getId());
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getCreatedDate()).isNotNull(),
                () -> assertThat(groupedTables).hasSize(2)
        );
    }

    @DisplayName("테이블 그룹을 해제할 수 있다.")
    @Test
    void ungroup() {
        TableGroupResponse given = tableGroupService.create(Arrays.asList(tableA.getId(), tableB.getId()));

        tableGroupService.ungroup(given.getId());

        List<OrderTable> groupedTables = orderTableRepository.findAllByTableGroupId(given.getId());
        assertThat(groupedTables).hasSize(0);
    }
}
