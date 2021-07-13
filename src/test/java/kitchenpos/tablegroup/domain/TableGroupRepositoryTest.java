package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@DataJpaTest
class TableGroupRepositoryTest {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("기본 저장 확인")
    void save() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, true));
        TableGroup tableGroup1 = new TableGroup();
        tableGroup1.addOrderTable(orderTable);
        TableGroup tableGroup = tableGroupRepository.save(tableGroup1);

        assertThat(tableGroup.getId()).isNotNull();
    }
}
