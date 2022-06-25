package kitchenpos.application;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class TableGroupServiceTest extends ServiceTest {
    
    @Autowired
    ServiceTestHelper serviceTestHelper;

    @Test
    void 테이블그룹_지정() {
        int numberOfTables = 2;
        TableGroup savedTableGroup = serviceTestHelper.테이블그룹_지정됨(numberOfTables);

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).hasSize(numberOfTables);
    }

    @Test
    void 테이블그룹_지정_저장되지않은_테이블로_그룹지정을_시도하는경우() {
        OrderTable newOrderTable = OrderTableFixtureFactory.createEmptyOrderTable();
        OrderTable newOrderTable2 = OrderTableFixtureFactory.createEmptyOrderTable();

        assertThatIllegalArgumentException().isThrownBy(()->{
            serviceTestHelper.테이블그룹_지정됨(newOrderTable,newOrderTable2);
        });
    }

    @Test
    void 테이블그룹_지정_테이블이_2개미만인경우() {
        int numberOfTables = 1;
        assertThatIllegalArgumentException().isThrownBy(()->{
            serviceTestHelper.테이블그룹_지정됨(numberOfTables);
        });
    }

    @Test
    void 테이블그룹_지정_비어있지않은_테이블이_포함된_경우() {
        OrderTable emptyTable = serviceTestHelper.빈테이블_생성됨();
        OrderTable notEmptyTable = serviceTestHelper.비어있지않은테이블_생성됨(3);

        assertThatIllegalArgumentException().isThrownBy(()->{
            serviceTestHelper.테이블그룹_지정됨(emptyTable,notEmptyTable);
        });
    }

    @Test
    void 테이블그룹_지정_다른_테이블그룹에_포함된_테이블이_있는_경우() {
        OrderTable emptyTable1 = serviceTestHelper.빈테이블_생성됨();
        OrderTable emptyTable2 = serviceTestHelper.빈테이블_생성됨();
        OrderTable emptyTable3 = serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.테이블그룹_지정됨(emptyTable1,emptyTable2);

        assertThatIllegalArgumentException().isThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(emptyTable2,emptyTable3));
    }

    @Test
    void 테이블그룹_지정해제() {
        //TODO: 주문 테스트 작성 후
    }

    @Test
    void 테이블그룹_지정해제_어떤테이블의_상태가_계산완료가_아닌경우() {
        //TODO: 주문 테스트 작성 후
    }
}
