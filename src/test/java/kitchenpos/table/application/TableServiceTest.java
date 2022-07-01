package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.CanNotMakeOrderTableException;
import kitchenpos.table.exception.NotExistTableException;
import kitchenpos.utils.ServiceTestHelper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {
    @Autowired
    ServiceTestHelper serviceTestHelper;

    @Autowired
    TableService tableService;

    @Test
    @DisplayName("빈 테이블 생성")
    void 빈_테이블_생성() {
        OrderTableResponse savedOrderTable = serviceTestHelper.빈테이블_생성됨();

        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    @DisplayName("비어있지 않은 테이블 생성")
    void 비어있지않은_테이블_생성() {
        int numberOfGuests = 4;
        OrderTableResponse savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(numberOfGuests);

        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("테이블 목록 조회")
    void 테이블_목록_조회() {
        serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.빈테이블_생성됨();
        List<OrderTableResponse> orderTables = tableService.list();
        assertThat(orderTables).hasSize(2);
    }

    @Test
    @DisplayName("빈 테이블로 변경")
    void 빈_테이블로_변경() {
        int numberOfGuests = 4;
        OrderTableResponse orderTable = serviceTestHelper.비어있지않은테이블_생성됨(numberOfGuests);
        OrderTableResponse updatedOrderTable = serviceTestHelper.빈테이블로_변경(orderTable.getId());

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isTrue();
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("비어있지않은 테이블로 변경")
    void 비어있지않은_테이블로_변경() {
        OrderTableResponse orderTable = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse updatedOrderTable = serviceTestHelper.비어있지않은테이블로_변경(orderTable.getId());

        assertThat(updatedOrderTable.getId()).isEqualTo(orderTable.getId());
        assertThat(updatedOrderTable.isEmpty()).isFalse();
        assertThat(updatedOrderTable.getNumberOfGuests()).isZero();
    }

    @Test
    @DisplayName("존재하지 않는 테이블의 경우 공석여부 변경 시도시 실패")
    void 테이블_공석상태변경_테이블이_존재하지않는경우() {
        Long notExistTableId = -1L;
        assertThatThrownBy(() -> serviceTestHelper.비어있지않은테이블로_변경(notExistTableId))
                .isInstanceOf(NotExistTableException.class);
    }

    @Test
    @DisplayName("테이블그룹에 포함된 테이블의 경우 공석여부 변경 시도시 실패")
    void 테이블_공석상태변경_테이블그룹에_포함된경우() {
        OrderTableResponse emptyTable = serviceTestHelper.빈테이블_생성됨();
        OrderTableResponse emptyTable2 = serviceTestHelper.빈테이블_생성됨();
        serviceTestHelper.테이블그룹_지정됨(emptyTable, emptyTable2);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.비어있지않은테이블로_변경(emptyTable.getId()));
    }

    @Test
    @DisplayName("주문의 상태가 계산완료가 아닌 경우 공석여부 변경 시도시 실패")
    void 테이블_공석상태변경_주문이_조리_식사상태인경우() {
        OrderTableResponse table = serviceTestHelper.비어있지않은테이블_생성됨(3);
        OrderResponse order = 테이블에_임시_주문_추가(table.getId());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.빈테이블로_변경(table.getId()));
    }

    @Test
    @DisplayName("테이블 인원수 변경")
    void 테이블_인원수_변경() {
        OrderTableResponse savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(4);
        int updatedNumberOfGuests = 3;
        OrderTableResponse updatedOrderTable = serviceTestHelper.테이블_인원수_변경(savedOrderTable.getId(), updatedNumberOfGuests);

        assertThat(updatedOrderTable.getId()).isEqualTo(savedOrderTable.getId());
        assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(updatedNumberOfGuests);
    }

    @Test
    @DisplayName("인원수가 음수일때 테이블 인원수 변경 실패")
    void 테이블_인원수_변경_음수로_변경시도() {
        OrderTableResponse savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(4);

        int invalidNumberOfGuests = -5;
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블_인원수_변경(savedOrderTable.getId(), invalidNumberOfGuests));
    }

    @Test
    @DisplayName("빈 테이블인 경우 테이블 인원수 변경 실패")
    void 테이블_인원수_변경_빈테이블인_경우() {
        OrderTableResponse savedOrderTable = serviceTestHelper.빈테이블_생성됨();
        int updatedNumberOfGuests = 4;
        assertThatIllegalArgumentException()
                .isThrownBy(() -> serviceTestHelper.테이블_인원수_변경(savedOrderTable.getId(), updatedNumberOfGuests));
    }

    @Test
    @DisplayName("테이블이 주문을 받을수 있는 상태인지 확인")
    void 테이블_주문가능여부() {
        OrderTableResponse savedOrderTable = serviceTestHelper.비어있지않은테이블_생성됨(4);
        assertThatNoException()
                .isThrownBy(() -> tableService.validateTableToMakeOrder(savedOrderTable.getId()));
    }

    @Test
    @DisplayName("테이블이 존재하지 않는 경우 주문 불가")
    void 테이블_주문불가능케이스_테이블이_없는경우() {
        assertThatThrownBy(() -> tableService.validateTableToMakeOrder(-1L))
                .isInstanceOf(NotExistTableException.class);
    }

    @Test
    @DisplayName("빈 테이블인 경우 주문 불가")
    void 테이블_주문불가능케이스_빈테이블() {
        OrderTableResponse savedOrderTable = serviceTestHelper.빈테이블_생성됨();
        assertThatThrownBy(() -> tableService.validateTableToMakeOrder(savedOrderTable.getId()))
                .isInstanceOf(CanNotMakeOrderTableException.class);
    }

    private OrderResponse 테이블에_임시_주문_추가(Long tableId) {
        MenuGroup menuGroup = serviceTestHelper.메뉴그룹_생성됨("메뉴그룹1");
        Product product1 = serviceTestHelper.상품_생성됨("상품1", 1000);
        MenuProduct menuProduct = MenuProductFixtureFactory.createMenuProduct(product1.getId(), 4);
        MenuDto menu = serviceTestHelper.메뉴_생성됨(menuGroup, "메뉴1", 4000, Lists.newArrayList(menuProduct));
        OrderLineItemDto orderLineItem = OrderLineItemFixtureFactory.createOrderLine(menu.getId(), 3);
        return serviceTestHelper.주문_생성됨(tableId, Lists.newArrayList(orderLineItem));
    }
}
