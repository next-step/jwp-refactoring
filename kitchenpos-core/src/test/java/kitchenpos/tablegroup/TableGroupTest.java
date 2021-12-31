package kitchenpos.tablegroup;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.validator.OrderTableTableGroupCreateValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TableGroupTest {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
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

    @DisplayName("2개 미만일 경우 생성할 수 없다.")
    @Test
    void createFail() {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> TableGroup.create(Collections.singletonList(2L), orderTableCreateTableGroupValidator);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
