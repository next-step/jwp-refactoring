package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderLinkerImplementation;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupLinker;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TableGroupServiceRealTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    private TableGroupLinker tableGroupLinker = new OrderLinkerImplementation(orderRepository);

    private TableGroupService tableGroupService;

    private OrderTable table1;
    private OrderTable table2;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository, tableGroupLinker);
        table1 = new OrderTable(null, 2,true);
        table2 = new OrderTable(null, 4,true);
        orderTableRepository.saveAll(Arrays.asList(table1, table2));
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        TableGroupResponse response = tableGroupService.create(new TableGroupRequest(Arrays.asList(table1, table2)));

        List<TableGroup> groups = tableGroupRepository.findAll();
        assertThat(groups).isNotEmpty();
        assertThat(groups.get(0).getId()).isNotNull();
    }
}
