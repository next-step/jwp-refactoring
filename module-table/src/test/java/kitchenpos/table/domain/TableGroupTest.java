package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import kitchenpos.table.application.TableGroupValidator;
import kitchenpos.table.exception.CreateTableGroupException;
import kitchenpos.table.fixture.OrderTableFixtureFactory;
import kitchenpos.table.fixture.TableGroupFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupValidator tableGroupValidator;

    private OrderTable 주문테이블_1;
    private OrderTable 주문테이블_2;
    private TableGroup 단체;

    @BeforeEach
    void setUp() {
        주문테이블_1 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문테이블_2 = OrderTableFixtureFactory.createWithGuest(true, 4);
        단체 = TableGroupFixtureFactory.create(1L);

        주문테이블_1 = orderTableRepository.save(주문테이블_1);
        주문테이블_2 = orderTableRepository.save(주문테이블_2);
        단체 = tableGroupRepository.save(단체);
    }

    @DisplayName("TableGroup을 생성할 수 있다. (OrderTables)")
    @Test
    void create01() {
        // given & when & then
        assertThatNoException().isThrownBy(
                () -> tableGroupValidator.validateGrouping(Lists.newArrayList(주문테이블_1, 주문테이블_2))
        );
    }

    @DisplayName("TableGroup 생성 시 OrderTables가 존재하지 않으면 생성할 수 없다.")
    @Test
    void create02() {
        // given
        List<OrderTable> orderTables = Collections.emptyList();

        // when & then
        assertThrows(CreateTableGroupException.class, () -> tableGroupValidator.validateGrouping(orderTables));
    }

    @DisplayName("TableGroup 생성 시 OrderTables 이 하나만 존재하면 생성할 수 없다.")
    @Test
    void create03() {
        // given
        List<OrderTable> orderTables = Lists.newArrayList(주문테이블_1);

        // when & then
        assertThrows(CreateTableGroupException.class, () -> tableGroupValidator.validateGrouping(orderTables));
    }

    @DisplayName("TableGroup 생성 시 OrderTables 중 비어있지 않은 OrderTable이 존재하면 생성할 수 없다.")
    @Test
    void create04() {
        // given
        주문테이블_1.changeEmpty(false);
        List<OrderTable> orderTables = Lists.newArrayList(주문테이블_1, 주문테이블_2);

        // when & then
        assertThrows(CreateTableGroupException.class, () -> tableGroupValidator.validateGrouping(orderTables));
    }

    @DisplayName("TableGroup 생성 시 OrderTables 중 이미 TableGroup에 속해있는 OrderTable이 존재하면 생성할 수 없다.")
    @Test
    void create05() {
        // given
        주문테이블_1.mappedByTableGroup(단체.getId());
        List<OrderTable> orderTables = Lists.newArrayList(주문테이블_1, 주문테이블_2);

        // when & then
        assertThrows(CreateTableGroupException.class, () -> tableGroupValidator.validateGrouping(orderTables));
    }
}