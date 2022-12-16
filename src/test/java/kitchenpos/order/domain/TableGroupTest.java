package kitchenpos.order.domain;

import kitchenpos.JpaEntityTest;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("테이블 그룹(단체테이블) 도메인 테스트")
public class TableGroupTest extends JpaEntityTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

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
        flushAndClear();

        // when / then
        assertThatThrownBy(() -> new TableGroup(테이블들))
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
        flushAndClear();

        // when
        TableGroup tableGroup = tableGroupRepository.getOne(savedTableGroup.getId());
        tableGroup.unGroup();
        flushAndClear();

        // then
        OrderTable table1 = orderTableRepository.getOne(테이블1.getId());
        OrderTable table2 = orderTableRepository.getOne(테이블2.getId());
        assertAll(
                () -> assertThat(table1.isGrouping()).isFalse(),
                () -> assertThat(table2.isGrouping()).isFalse()
        );
    }
}
