package kitchenpos.table.domain;

import kitchenpos.TableApplication;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.utils.DatabaseCleanup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = TableApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("테이블 그룹(단체테이블) 도메인 테스트")
public class TableGroupTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
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

        // when
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(테이블들));
        savedTableGroup.enGroup(tableGroupValidator);

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2),
                () -> assertThat(테이블1.isGrouping()).isTrue(),
                () -> assertThat(테이블2.isGrouping()).isTrue()
        );
    }

    @DisplayName("테이블이 비어있지 않은 경우 단체 테이블로 만들려고 할 때 예외 발생")
    @Test
    void createTableGroupException() {
        // given
        OrderTable 테이블1 = new OrderTable(4, true);
        OrderTable 테이블2 = new OrderTable(8, false);
        List<OrderTable> 테이블들 = Lists.newArrayList(테이블1, 테이블2);

        // when
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(테이블들));

        // then
        assertThatThrownBy(() -> savedTableGroup.enGroup(tableGroupValidator))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("테이블 ungroup 테스트")
    @Test
    void unGroupTable() {
        // given
        OrderTable 테이블1 = new OrderTable(4, true);
        OrderTable 테이블2 = new OrderTable(8, true);
        List<OrderTable> 테이블들 = Lists.newArrayList(테이블1, 테이블2);
        TableGroup 테이블그룹 = tableGroupRepository.save(new TableGroup(테이블들));
        테이블그룹.enGroup(tableGroupValidator);

        // when
        테이블그룹.unGroup(tableGroupValidator);
        TableGroup 목표테이블그룹 = tableGroupRepository.save(테이블그룹);

        // then
        assertAll(
                () -> assertThat(테이블1.isGrouping()).isFalse(),
                () -> assertThat(테이블2.isGrouping()).isFalse(),
                () -> assertThat(목표테이블그룹.getOrderTables()).isEmpty()
        );
    }
}
