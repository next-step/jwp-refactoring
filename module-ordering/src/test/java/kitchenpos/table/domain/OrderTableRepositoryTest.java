package kitchenpos.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    private Long tableGroup1Id = 1L;
    private OrderTable orderTable1 = new OrderTable(tableGroup1Id, 0, true);
    private OrderTable orderTable2 = new OrderTable(tableGroup1Id, 0, true);

    @BeforeEach
    void setUp() {

    }

    @Test
    void create() {
        OrderTable persistOrderTable = orderTableRepository.save(orderTable1);

        Assertions.assertThat(persistOrderTable.getId()).isEqualTo(orderTable1.getId());
        Assertions.assertThat(persistOrderTable.getTableGroupId()).isEqualTo(orderTable1.getTableGroupId());
        Assertions.assertThat(persistOrderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        Assertions.assertThat(persistOrderTable.isEmpty()).isEqualTo(orderTable1.isEmpty());
    }

    @DisplayName("주문테이블 ID로 해당하는 주문테이블들을 찾는다.")
    @Test
    void findAllByIdInTest() {
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        Assertions.assertThat(orderTables).hasSize(2);
    }

    @DisplayName("테이블그룹 ID로 해당하는 주문테이블들을 찾는다.")
    @Test
    void findAllByTableGroupIdTest() {
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup1Id);
        Assertions.assertThat(orderTables).hasSize(2);
    }

}
