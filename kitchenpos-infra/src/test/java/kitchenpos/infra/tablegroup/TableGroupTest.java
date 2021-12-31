package kitchenpos.infra.tablegroup;

import kitchenpos.core.domain.OrderTable;
import kitchenpos.core.validator.OrderTableTableGroupCreateValidator;
import kitchenpos.core.domain.TableGroup;
import kitchenpos.infra.ordertable.JpaOrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TableGroupTest {
    @Autowired
    private JpaOrderTableRepository orderTableRepository;
    @Autowired
    private JpaTableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableTableGroupCreateValidator orderTableCreateTableGroupValidator;

    @DisplayName("단체 지정은 아이디, 생성시간, 주문 테이블로 구성되어 있다.")
    @Test
    void create() {
        // given
        final List<OrderTable> orderTables = orderTableRepository.saveAll(
                Arrays.asList(
                        OrderTable.of(13, true),
                        OrderTable.of(13, true))
        );
        // when
        final TableGroup tableGroup = TableGroup.create(Arrays.asList(orderTables.get(0).getId(), orderTables.get(1).getId()), orderTableCreateTableGroupValidator);
        final TableGroup actual = tableGroupRepository.save(tableGroup);
        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTableIds().size()).isEqualTo(2)
        );
    }
}
