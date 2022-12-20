package kitchenpos.table.domain;

import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.utils.DatabaseCleanup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({DatabaseCleanup.class, OrderTableValidator.class, TableGroupValidator.class})
@DisplayName("테이블 그룹(단체테이블) 도메인 테스트")
public class TableGroupTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TableGroupValidator tableGroupValidator;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }

    @DisplayName("단체테이블 객체 생성")
    @Test
    void createTableGroup() {
        // given
        OrderTable 테이블1 = new OrderTable(4, true);
        OrderTable 테이블2 = new OrderTable(8, true);
        List<OrderTable> 테이블들 = Lists.newArrayList(테이블1, 테이블2);
        orderTableRepository.saveAll(테이블들);

        // when
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(테이블들));
        savedTableGroup.enGroup(getTableGroupValidator());
        orderTableRepository.saveAll(savedTableGroup.getOrderTables());
        flushAndClear();

        // then
        TableGroup tableGroup = tableGroupRepository.getOne(savedTableGroup.getId());
        OrderTable table1 = orderTableRepository.getOne(테이블1.getId());
        OrderTable table2 = orderTableRepository.getOne(테이블2.getId());
        assertAll(
                () -> assertThat(tableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(table1.isGrouping()).isTrue(),
                () -> assertThat(table2.isGrouping()).isTrue()
        );
    }

    @DisplayName("테이블이 비어있지 않은 경우 단체 테이블로 만들려고 할 때 예외 발생")
    @Test
    void createTableGroupException() {
        // given
        OrderTable 테이블1 = new OrderTable(4, true);
        OrderTable 테이블2 = new OrderTable(8, false);
        List<OrderTable> 테이블들 = Lists.newArrayList(테이블1, 테이블2);
        orderTableRepository.saveAll(테이블들);

        // when
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(테이블들));
        flushAndClear();

        // then
        assertThatThrownBy(() -> savedTableGroup.enGroup(getTableGroupValidator()))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("테이블 ungroup 테스트")
    @Test
    void unGroupTable() {
        // given
        OrderTable 테이블1 = new OrderTable(4, true);
        OrderTable 테이블2 = new OrderTable(8, true);
        List<OrderTable> 테이블들 = Lists.newArrayList(테이블1, 테이블2);
        orderTableRepository.saveAll(테이블들);
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(테이블들));
        savedTableGroup.enGroup(getTableGroupValidator());
        orderTableRepository.saveAll(savedTableGroup.getOrderTables());
        flushAndClear();

        // when
        TableGroup tableGroup = tableGroupRepository.getOne(savedTableGroup.getId());
        tableGroup.unGroup(getTableGroupValidator());
        tableGroup = tableGroupRepository.save(tableGroup);
        flushAndClear();

        // then
        OrderTable table1 = orderTableRepository.getOne(테이블1.getId());
        OrderTable table2 = orderTableRepository.getOne(테이블2.getId());
        TableGroup tableGroup1 = tableGroupRepository.getOne(tableGroup.getId());
        assertAll(
                () -> assertThat(table1.isGrouping()).isFalse(),
                () -> assertThat(table2.isGrouping()).isFalse(),
                () -> assertThat(tableGroup1.getOrderTables()).isEmpty()
        );
    }

    private TableGroupValidator getTableGroupValidator() {
        return tableGroupValidator;
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
