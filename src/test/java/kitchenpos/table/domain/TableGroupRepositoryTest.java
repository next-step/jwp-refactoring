package kitchenpos.table.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:scripts/data.sql")
public class TableGroupRepositoryTest {

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("단체 지정")
    void save() {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L));

        TableGroup persist = tableGroupRepository.save(TableGroup.fromOrderTables(orderTables));

        //query 확인
        tableGroupRepository.flush();

        assertNotNull(persist.getCreatedDate());
    }

    @Test
    @DisplayName("단체 해제")
    void ungroup() {
        save();

        TableGroup tableGroup = tableGroupRepository.findAll().get(0);

        assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);

        tableGroup.clearOrderTable();

        //query 확인
        tableGroupRepository.flush();

        assertThat(tableGroup.getOrderTables().size()).isEqualTo(0);

    }
}
