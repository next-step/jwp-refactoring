package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.CannotMakeTableGroupException;
import kitchenpos.table.exception.CannotUngroupException;
import kitchenpos.table.exception.NotExistTableException;
import kitchenpos.utils.ServiceTestHelper;
import org.assertj.core.util.Lists;
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
        assertThatThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(newOrderTable, newOrderTable2))
                .isInstanceOf(NotExistTableException.class);
    }

    @Test
    @DisplayName("2개 미만의 테이블을 그룹 지정할 경우 실패")
    void 테이블그룹_지정_테이블이_2개미만인경우() {
        int numberOfTables = 1;
        assertThatThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(numberOfTables))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("비어있지않은 테이블이 포함된 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_비어있지않은_테이블이_포함된_경우() {
        OrderTableResponse emptyTable = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse notEmptyTable = serviceTestHelper.비어있지않은테이블_생성됨(3);
        assertThatThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(emptyTable, notEmptyTable))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("다른 테이블 그룹에 포함된 테이블이 있을 경우 테이블 그룹 지정 실패")
    void 테이블그룹_지정_다른_테이블그룹에_포함된_테이블이_있는_경우() {
        OrderTableResponse emptyTable1 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse emptyTable2 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse emptyTable3 = serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.테이블그룹_지정됨(emptyTable1, emptyTable2);
        assertThatThrownBy(() -> serviceTestHelper.테이블그룹_지정됨(emptyTable2, emptyTable3))
                .isInstanceOf(CannotMakeTableGroupException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 주문이 없는 경우 테이블 그룹 해제가능")
    void 테이블그룹_지정해제_주문이_없는_경우() {
        OrderTableResponse table1 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse table2 = serviceTestHelper.빈테이블_생성됨();
        TableGroupResponse tableGroup = serviceTestHelper.테이블그룹_지정됨(table1, table2);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTableResponse> orderTables = tableService.findAllByTableGroupId(tableGroup.getId());
        assertThat(orderTables).isEmpty();
    }

    @Test
    @DisplayName("모든 주문 테이블이 계산완료된 경우 테이블 그룹 해제 성공")
    void 테이블그룹_지정해제_계산완료시() {
        OrderTableResponse table1 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse table2 = serviceTestHelper.빈테이블_생성됨();
        TableGroupResponse tableGroup = serviceTestHelper.테이블그룹_지정됨(table1, table2);
        OrderResponse order1 = 주문생성됨(table1);
        OrderResponse order2 = 주문생성됨(table2);
        serviceTestHelper.주문상태_변경(order1.getId(), OrderStatus.COMPLETION);
        serviceTestHelper.주문상태_변경(order2.getId(), OrderStatus.COMPLETION);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTableResponse> orderTables = tableService.findAllByTableGroupId(tableGroup.getId());
        assertThat(orderTables).isEmpty();
    }

    @Test
    @DisplayName("계산완료되지 않은 주문 테이블이 있는 경우 테이블 그룹 해제 실패")
    void 테이블그룹_지정해제_계산완료전() {
        OrderTableResponse table1 = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse table2 = serviceTestHelper.빈테이블_생성됨();
        TableGroupResponse tableGroup = serviceTestHelper.테이블그룹_지정됨(table1, table2);
        주문생성됨(table1);
        Long tableGroupId = tableGroup.getId();
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(CannotUngroupException.class);
    }

    private OrderResponse 주문생성됨(OrderTableResponse orderTable) {
        MenuGroup menuGroup = serviceTestHelper.메뉴그룹_생성됨("메뉴그룹1");
        Product product1 = serviceTestHelper.상품_생성됨("상품1", 1000);
        Product product2 = serviceTestHelper.상품_생성됨("상품2", 2000);
        MenuProduct menuProduct1 = MenuProductFixtureFactory.createMenuProduct(product1.getId(), 4);
        MenuProduct menuProduct2 = MenuProductFixtureFactory.createMenuProduct(product2.getId(), 2);
        MenuDto menu1 = serviceTestHelper.메뉴_생성됨(menuGroup, "메뉴1", 4000, Lists.newArrayList(menuProduct1));
        MenuDto menu2 = serviceTestHelper.메뉴_생성됨(menuGroup, "메뉴2", 4000, Lists.newArrayList(menuProduct2));
        OrderLineItemDto orderLineItem1 = OrderLineItemFixtureFactory.createOrderLine(menu1.getId(), 3);
        OrderLineItemDto orderLineItem2 = OrderLineItemFixtureFactory.createOrderLine(menu2.getId(), 3);

        return serviceTestHelper.주문_생성됨(orderTable.getId(),
                Lists.newArrayList(orderLineItem1, orderLineItem2));
    }
}
