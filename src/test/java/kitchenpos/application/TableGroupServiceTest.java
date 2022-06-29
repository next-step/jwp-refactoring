package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    ServiceTestHelper serviceTestHelper;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹 지정")
    void 테이블그룹_지정() {
        int numberOfTables = 2;
        TableGroupResponse savedTableGroup = serviceTestHelper.테이블그룹_지정됨(numberOfTables);

        List<OrderTableResponse> orderTables = savedTableGroup.getOrderTableResponses();
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(orderTables).hasSize(numberOfTables);
        orderTables.forEach(table -> assertThat(table.getTableGroupId()).isEqualTo(savedTableGroup.getId()));
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 테이블 조회")
    void 테이블그룹에_포함된_테이블목록() {
        int numberOfTables = 2;
        TableGroupResponse savedTableGroup = serviceTestHelper.테이블그룹_지정됨(numberOfTables);

        List<OrderTableResponse> orderTables = tableService.findAllByTableGroupId(savedTableGroup.getId());
        assertThat(orderTables).hasSize(2);
    }

    @Test
    @DisplayName("존재하지 않는 테이블이 포함된 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_저장되지않은_테이블로_그룹지정을_시도하는경우() {
        OrderTableResponse newOrderTable = OrderTableFixtureFactory.createEmptyOrderTableResponse(-1L);
        OrderTableResponse newOrderTable2 = OrderTableFixtureFactory.createEmptyOrderTableResponse(-2L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(newOrderTable, newOrderTable2));
    }

    @Test
    @DisplayName("2개 미만의 테이블을 그룹 지정할 경우 실패")
    void 테이블그룹_지정_테이블이_2개미만인경우() {
        int numberOfTables = 1;
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(numberOfTables));
    }

    @Test
    @DisplayName("비어있지않은 테이블이 포함된 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_비어있지않은_테이블이_포함된_경우() {
        OrderTableResponse emptyTable = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse notEmptyTable = serviceTestHelper.비어있지않은테이블_생성됨(3);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(emptyTable, notEmptyTable));
    }

    @Test
    @DisplayName("다른 테이블 그룹에 포함된 테이블이 있을 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_다른_테이블그룹에_포함된_테이블이_있는_경우() {
        OrderTableResponse emptyTable1 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse emptyTable2 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse emptyTable3 = serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.테이블그룹_지정됨(emptyTable1, emptyTable2);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(emptyTable2, emptyTable3));
    }

    @Test
    @DisplayName("테이블 그룹 해제")
    void 테이블그룹_지정해제_빈테이블인_경우() {
        OrderTableResponse table1 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse table2 = serviceTestHelper.빈테이블_생성됨();
        TableGroupResponse tableGroup = serviceTestHelper.테이블그룹_지정됨(table1, table2);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTableResponse> orderTables = tableService.findAllByTableGroupId(tableGroup.getId());
        assertThat(orderTables).hasSize(0);
    }
}
