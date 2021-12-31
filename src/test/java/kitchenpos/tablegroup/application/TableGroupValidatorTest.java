package kitchenpos.tablegroup.application;

import kitchenpos.common.exception.MinimumOrderTableNumberException;
import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.common.exception.OrderStatusNotCompletedException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.TableGroupValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("테이블 그룹 벨리데이터 관련 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {
    @Mock
    private TableGroupService tableGroupService;

    private TableGroupValidator tableGroupValidator;

    private Product 짜장면_상품;
    private OrderLineItem 짜장면_주문1;
    private OrderLineItem 짜장면_주문2;
    private Order 주문;
    private Menu 짜장면;
    private MenuProduct 짜장면_하나;
    private MenuProduct 짜장면_두개;

    private OrderTable 주문테이블1번;
    private OrderTable 주문테이블2번;
    private OrderTable 주문테이블3번;
    private OrderTable 주문테이블4번;

    private TableGroup 단체테이블1번;

    private TableGroupRequest 단체테이블1번_요청;
    private TableGroupRequest 단체테이블2번_요청;
    private TableGroupRequest 단체테이블3번_요청;

    @BeforeEach
    void setUp() {
        tableGroupValidator = new TableGroupValidator(tableGroupService);

        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_하나 = new MenuProduct(1L, new Menu(), 짜장면_상품.getId(), 1);
        짜장면_두개 = new MenuProduct(2L, new Menu(), 짜장면_상품.getId(), 2);
        짜장면 = new Menu("짜장면", 10000, new MenuGroup(), Lists.newArrayList(짜장면_하나, 짜장면_두개));

        짜장면_주문1 = new OrderLineItem(주문, 짜장면.getId(), 10);
        짜장면_주문2 = new OrderLineItem(주문, 짜장면.getId(), 3);

        주문테이블1번 = new OrderTable(null, 0);
        주문테이블2번 = new OrderTable(null, 0);
        단체테이블1번 = new TableGroup(1L, Lists.newArrayList(주문테이블1번, 주문테이블2번));

        주문테이블3번 = new OrderTable(null, 3);
        주문테이블4번 = new OrderTable(null, 3);

        단체테이블1번_요청 = new TableGroupRequest(Lists.newArrayList(주문테이블1번, 주문테이블2번));
        단체테이블2번_요청 = new TableGroupRequest(Lists.newArrayList());
        단체테이블3번_요청 = new TableGroupRequest(Lists.newArrayList(주문테이블3번, 주문테이블4번));
    }

    @DisplayName("생성 시 주문 테이블은 반드시 존재하고 2개이상 있어야 한다.")
    @Test
    void createTableGroupAtLeast2OrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            테이블_그룹_생성을_확인한다(단체테이블2번_요청);

            // then
        }).isInstanceOf(MinimumOrderTableNumberException.class);
    }

    @DisplayName("생성 시 주문 테이블은 비어있어야 한다.")
    @Test
    void createTableGroupEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            테이블_그룹_생성을_확인한다(단체테이블3번_요청);

            // then
        }).isInstanceOf(NotEmptyOrderTableStatusException.class);
    }

    @DisplayName("테이블 그룹은 주문 상태가 cooking or meal 이 아니어야 한다.")
    @Test
    void ungroupTableGroupEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            단체테이블1번 = new TableGroup(1L, Arrays.asList(주문테이블1번, 주문테이블2번));

            주문테이블1번.changeNumberOfGuests(3);

            주문 = new Order(주문테이블1번, Arrays.asList(짜장면_주문1, 짜장면_주문2));
            주문.changeOrderStatus(OrderStatus.COOKING);

            주문테이블1번.addOrder(주문);

            // when
            테이블_그룹_해제를_확인한다(단체테이블1번);

            // then
        }).isInstanceOf(OrderStatusNotCompletedException.class);
    }

    private void 테이블_그룹_생성을_확인한다(TableGroupRequest tableGroupRequest) {
        tableGroupValidator.validateCreateTableGroup(tableGroupRequest);
    }

    private void 테이블_그룹_해제를_확인한다(TableGroup tableGroup) {
        tableGroupValidator.validateUnGroupTableGroup(tableGroup);
    }
}