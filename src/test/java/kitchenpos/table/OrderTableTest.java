package kitchenpos.table;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderTableTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("테이블을 등록합니다.")
    void save() {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        OrderTable persistOrderTable = this.orderTableRepository.save(orderTable);

        // then
        assertThat(persistOrderTable.getId()).isNotNull();
    }

    @Test
    @DisplayName("테이블 ID로 특정 테이블을 조회합니다")
    void findById() {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable persistOrderTable = this.orderTableRepository.save(orderTable);

        // when
        OrderTable foundOrderTable = this.orderTableRepository.findById(persistOrderTable.getId()).get();

        // then
        assertThat(foundOrderTable.getId()).isEqualTo(persistOrderTable.getId());
    }

    @Test
    @DisplayName("전체 테이블을 조회합니다.")
    void findAll() {
        // when
        List<OrderTable> orderTables = this.orderTableRepository.findAll();

        // then
        assertThat(orderTables).hasSize(8);
    }

    @Test
    @DisplayName("테이블 ID로 특정 테이블들을 조회합니다.")
    void findAllByIdIn() {
        // given
        List<Long> ids = Arrays.asList(new Long[]{1L, 2L, 3L});

        // when
        List<OrderTable> orderTables = this.orderTableRepository.findAllByIdIn(ids);

        // then
        assertThat(orderTables).hasSize(3);
        assertThat(orderTables.stream().mapToLong(OrderTable::getId)).contains(1L, 2L, 3L);
    }

    @Test
    @DisplayName("테이블 그룹 ID로 해당 그룹에 포함된 테이블들을 조회합니다.")
    void findAllByTableGroupId() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup persistTableGroup = this.tableGroupRepository.save(tableGroup);
        List<OrderTable> orderTableOrigins = this.orderTableRepository.findAll();
        orderTableOrigins.stream().forEach(orderTable -> {
            orderTable.setTableGroup(persistTableGroup);
            this.orderTableRepository.save(orderTable);
        });

        // when
        List<OrderTable> orderTables = this.orderTableRepository.findAllByTableGroupId(persistTableGroup.getId());

        // then
        assertThat(orderTables).hasSize(10);
        assertThat(orderTables.stream().mapToLong(OrderTable::getId)).contains(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
    }
}
