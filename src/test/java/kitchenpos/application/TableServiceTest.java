package kitchenpos.application;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;
class TableServiceTest extends ServiceTest {
    @Autowired
    ServiceTestHelper serviceTestHelper;

    @Autowired
    TableService tableService;

    @Test
    void 빈_테이블_생성(){
        OrderTable savedOrderTable = serviceTestHelper.빈테이블_생성됨();

        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    void 비어있지않은_테이블_생성(){
        int numberOfGuests = 4;
        OrderTable savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(numberOfGuests);

        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void 테이블_목록_조회(){
        serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.빈테이블_생성됨();
        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void 빈_테이블로_변경(){
        int numberOfGuests = 4;
        OrderTable orderTable = serviceTestHelper.비어있지않은테이블_생성됨(numberOfGuests);

        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(true);
        OrderTable updatedOrderTable = tableService.changeEmpty(orderTable.getId(),param);

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isTrue();
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void 비어있지않은_테이블로_변경(){
        OrderTable orderTable = serviceTestHelper.빈테이블_생성됨();

        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(false);
        OrderTable updatedOrderTable = tableService.changeEmpty(orderTable.getId(),param);

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isFalse();
        assertThat(updatedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    void 테이블_공석상태변경_테이블이_존재하지않는경우(){
        OrderTable notSavedOrderTable = OrderTableFixtureFactory.createEmptyOrderTable();

        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(false);
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableService.changeEmpty(notSavedOrderTable.getId(),param);
        });
    }

    @Test
    void 테이블_공석상태변경_테이블그룹에_포함된경우(){
        OrderTable emptyTable = serviceTestHelper.빈테이블_생성됨();
        OrderTable emptyTable2 = serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.테이블그룹_지정됨(emptyTable,emptyTable2);

        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(false);
        assertThatIllegalArgumentException().isThrownBy(()->{
            tableService.changeEmpty(emptyTable.getId(),param);
        });
    }

    @Test
    void 테이블_공석상태변경_주문이_조리_식사상태인경우(){
        //TODO 주문 테스트 작성 후
    }

    @Test
    void 테이블_인원수_변경(){
        OrderTable savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(4);

        int updatedNumberOfGuests = 3;
        OrderTable param = OrderTableFixtureFactory.createParamForChangeNumberOfGuests(updatedNumberOfGuests);
        OrderTable updatedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),param);

        assertThat(updatedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(updatedNumberOfGuests);
    }

    @Test
    void 테이블_인원수_변경_음수로_변경시도(){
        OrderTable savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(4);

        int invalidNumberOfGuests = -5;
        OrderTable param = OrderTableFixtureFactory.createParamForChangeNumberOfGuests(invalidNumberOfGuests);

        assertThatIllegalArgumentException()
                .isThrownBy(()-> tableService.changeNumberOfGuests(savedOrderTable.getId(),param));
    }

    @Test
    void 테이블_인원수_변경_빈테이블인_경우(){
        OrderTable savedOrderTable = serviceTestHelper.빈테이블_생성됨();

        OrderTable param = OrderTableFixtureFactory.createParamForChangeNumberOfGuests(4);

        assertThatIllegalArgumentException()
                .isThrownBy(()-> tableService.changeNumberOfGuests(savedOrderTable.getId(),param));
    }

}
