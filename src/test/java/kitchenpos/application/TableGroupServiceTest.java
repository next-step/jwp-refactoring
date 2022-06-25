package kitchenpos.application;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

class TableGroupServiceTest extends ServiceTest {
    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void 테이블그룹_지정() {
        tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        List<OrderTable> orderTables = tableService.list();

        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).hasSize(2);
    }

    @Test
    void 테이블그룹_지정_저장되지않은_테이블로_그룹지정을_시도하는경우() {
        OrderTable newOrderTable = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable newOrderTable2 = OrderTableFixtureFactory.createEmptyOrderTable();

        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(Lists.newArrayList(newOrderTable,newOrderTable2));
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableGroupService.create(tableGroup);
        });
    }

    @Test
    void 테이블그룹_지정_테이블이_2개미만인경우() {
        OrderTable savedOrderTable = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(Lists.newArrayList(savedOrderTable));

        assertThatIllegalArgumentException().isThrownBy(()->{
            tableGroupService.create(tableGroup);
        });
    }

    @Test
    void 테이블그룹_지정_비어있지않은_테이블이_포함된_경우() {
        OrderTable savedOrderTable = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        OrderTable savedOrderTable2 = tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(3));

        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(Lists.newArrayList(savedOrderTable,savedOrderTable2));
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableGroupService.create(tableGroup);
        });
    }
    @Test
    void 테이블그룹_지정_다른_테이블그룹에_포함된_테이블이_있는_경우() {
        OrderTable savedOrderTable = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        OrderTable savedOrderTable2 = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        OrderTable savedOrderTable3 = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(Lists.newArrayList(savedOrderTable,savedOrderTable2));
        tableGroupService.create(tableGroup);

        TableGroup tableGroup2 = TableGroupFixtureFactory.createTableGroup(Lists.newArrayList(savedOrderTable2,savedOrderTable3));
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableGroupService.create(tableGroup2);
        });
    }

    @Test
    void 테이블그룹_지정해제() {
        //TODO: 주문 테스트 작성 후
    }
}
