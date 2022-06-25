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
class TableServiceTest extends ServiceTest {
    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void 빈_테이블_생성(){
        OrderTable orderTable = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    void 비어있지않은_테이블_생성(){
        int numberOfGuests = 4;
        OrderTable orderTable = OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests);
        OrderTable savedOrderTable = tableService.create(orderTable);
        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void 테이블_목록_조회(){
        tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void 빈_테이블로_변경(){
        int numberOfGuests = 4;
        OrderTable orderTable = tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
        OrderTable param = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable updatedOrderTable = tableService.changeEmpty(orderTable.getId(),param);

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isTrue();
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void 비어있지않은_테이블로_변경(){
        OrderTable orderTable = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        int numberOfGuests = 4;
        OrderTable param = OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests);
        OrderTable updatedOrderTable = tableService.changeEmpty(orderTable.getId(),param);

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isFalse();
        assertThat(updatedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    void 테이블_공석상태변경_테이블이_존재하지않는경우(){
        OrderTable orderTable = OrderTableFixtureFactory.createEmptyOrderTable();
        int numberOfGuests = 4;
        OrderTable param = OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests);
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableService.changeEmpty(orderTable.getId(),param);
        });
    }

    @Test
    void 테이블_공석상태변경_테이블그룹에_포함된경우(){
        OrderTable savedOrderTable = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        OrderTable savedOrderTable2 = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        TableGroup tableGroup = TableGroupFixtureFactory.createTableGroup(Lists.newArrayList(savedOrderTable,savedOrderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        int numberOfGuests = 4;
        OrderTable param = OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests);
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableService.changeEmpty(savedOrderTable.getId(),param);
        });
    }

    @Test
    void 테이블_공석상태변경_주문이_조리_식사상태인경우(){
        //TODO 주문 테스트 작성 후
    }

    @Test
    void 테이블_인원수_변경(){
        int numberOfGuests = 4;
        OrderTable savedOrderTable = tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
        int updatedNumberOfGuests = 3;
        OrderTable param = OrderTableFixtureFactory.createNotEmptyOrderTable(updatedNumberOfGuests);
        OrderTable updatedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),param);

        assertThat(updatedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(updatedNumberOfGuests);
    }

    @Test
    void 테이블_인원수_변경_음수로_변경시도(){
        int numberOfGuests = 4;
        OrderTable savedOrderTable = tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
        int invalidNumberOfGuests = -5;
        OrderTable param = OrderTableFixtureFactory.createNotEmptyOrderTable(invalidNumberOfGuests);
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableService.changeNumberOfGuests(savedOrderTable.getId(),param);
        });
    }

    @Test
    void 테이블_인원수_변경_빈테이블인_경우(){
        OrderTable savedOrderTable = tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
        int numberOfGuests = 4;
        OrderTable param = OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests);
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableService.changeNumberOfGuests(savedOrderTable.getId(),param);
        });
    }
}
