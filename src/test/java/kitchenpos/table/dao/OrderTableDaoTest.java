package kitchenpos.table.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OrderTableDaoTest {

    @Autowired
    OrderTableDao orderTableDao;

    @Test
    @DisplayName("tableId in 검색 테스트")
    void findByIdIn(){
        // given
        OrderTable savedTable1 = orderTableDao.save(new OrderTable(true));
        OrderTable savedTable2 = orderTableDao.save(new OrderTable(true));

        // when
        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(Arrays.asList(
                savedTable1.getId(), savedTable2.getId()
        ));

        // then
        assertThat(orderTables).hasSize(2);
        assertThat(orderTables).containsExactly(
                savedTable1, savedTable2
        );
    }
}
