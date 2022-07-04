package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.CannotMakeTableGroupException;
import kitchenpos.table.exception.NotExistTableException;
import kitchenpos.table.application.fixture.OrderTableDtoFixtureFactory;
import kitchenpos.table.application.util.TableContextServiceBehavior;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    TableContextServiceBehavior tableContextServiceBehavior;

    @Autowired
    TableService tableService;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹 지정")
    void 테이블그룹_지정() {
        int numberOfTables = 2;
        TableGroupResponse savedTableGroup = tableContextServiceBehavior.테이블그룹_지정됨(numberOfTables);

        List<OrderTableResponse> orderTables = savedTableGroup.getOrderTableResponses();
        assertThat(savedTableGroup.getId()).isNotNull();

        Assertions.assertAll("테이블 그룹에 포함된 테이블들을 확인한다."
                , () -> assertThat(orderTables).hasSize(numberOfTables)
                , () -> orderTables.forEach(
                        table -> assertThat(table.getTableGroupId()).isEqualTo(savedTableGroup.getId()))
        );
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 테이블 조회")
    void 테이블그룹에_포함된_테이블목록() {
        int numberOfTables = 2;
        TableGroupResponse savedTableGroup = tableContextServiceBehavior.테이블그룹_지정됨(numberOfTables);

        List<OrderTableResponse> orderTables = tableService.findAllByTableGroupId(savedTableGroup.getId());
        assertThat(orderTables).hasSize(2);
    }

    @Test
    @DisplayName("존재하지 않는 테이블이 포함된 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_저장되지않은_테이블로_그룹지정을_시도하는경우() {
        OrderTableResponse newOrderTable = OrderTableDtoFixtureFactory.createEmptyOrderTableResponse(-1L);
        OrderTableResponse newOrderTable2 = OrderTableDtoFixtureFactory.createEmptyOrderTableResponse(-2L);
        assertThatThrownBy(() -> tableContextServiceBehavior.테이블그룹_지정됨(newOrderTable, newOrderTable2))
                .isInstanceOf(NotExistTableException.class);
    }

    @Test
    @DisplayName("2개 미만의 테이블을 그룹 지정할 경우 실패")
    void 테이블그룹_지정_테이블이_2개미만인경우() {
        int numberOfTables = 1;
        assertThatThrownBy(() -> tableContextServiceBehavior.테이블그룹_지정됨(numberOfTables))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("비어있지않은 테이블이 포함된 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_비어있지않은_테이블이_포함된_경우() {
        OrderTableResponse emptyTable = tableContextServiceBehavior.빈테이블_생성됨();
        OrderTableResponse notEmptyTable = tableContextServiceBehavior.비어있지않은테이블_생성됨(3);
        assertThatThrownBy(() -> tableContextServiceBehavior.테이블그룹_지정됨(emptyTable, notEmptyTable))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("다른 테이블 그룹에 포함된 테이블이 있을 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_다른_테이블그룹에_포함된_테이블이_있는_경우() {
        OrderTableResponse emptyTable1 = tableContextServiceBehavior.빈테이블_생성됨();
        OrderTableResponse emptyTable2 = tableContextServiceBehavior.빈테이블_생성됨();
        OrderTableResponse emptyTable3 = tableContextServiceBehavior.빈테이블_생성됨();
        tableContextServiceBehavior.테이블그룹_지정됨(emptyTable1, emptyTable2);
        assertThatThrownBy(() -> tableContextServiceBehavior.테이블그룹_지정됨(emptyTable2, emptyTable3))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 주문이 없는 경우 테이블 그룹 해제가능")
    void 테이블그룹_지정해제_주문이_없는_경우() {
        OrderTableResponse table1 = tableContextServiceBehavior.빈테이블_생성됨();
        OrderTableResponse table2 = tableContextServiceBehavior.빈테이블_생성됨();
        TableGroupResponse tableGroup = tableContextServiceBehavior.테이블그룹_지정됨(table1, table2);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTableResponse> orderTables = tableService.findAllByTableGroupId(tableGroup.getId());
        assertThat(orderTables).isEmpty();
    }
}
