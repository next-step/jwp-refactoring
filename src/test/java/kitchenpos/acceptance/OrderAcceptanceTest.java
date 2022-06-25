package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.helper.KitchenPosBehaviors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderAcceptanceTest extends AcceptanceTest {

    Product product;
    MenuGroup menuGroup;
    Menu menu;
    OrderTable orderTable1;
    OrderTable orderTable2;

    @BeforeEach
    public void setUp(){
        super.setUp();
        product = KitchenPosBehaviors.상품_생성됨("상품1",10000);
        menuGroup = KitchenPosBehaviors.메뉴그룹_생성됨("메뉴그룹1");
        MenuProduct menuProduct = MenuProductFixtureFactory.createMenuProduct(product.getId(),1);
        menu = KitchenPosBehaviors.메뉴_생성됨(MenuFixtureFactory.createMenu(menuGroup,"강정치킨 한마리",10000, Lists.newArrayList(menuProduct)));
        orderTable1 = KitchenPosBehaviors.테이블_생성됨(OrderTableFixtureFactory.createEmptyOrderTable());
        orderTable2 = KitchenPosBehaviors.테이블_생성됨(OrderTableFixtureFactory.createEmptyOrderTable());
    }

    /**
     * Scenario : 한 테이블 손님
     * Given
     * 상품, 메뉴그룹, 메뉴, 테이블이 등록되어있다.
     *
     * When / Then
     * 3명의 손님이 와서 / 테이블의 상태를 비어있지 않은 상태로 변경한다.
     * 주문내용을 받고 / 테이블에 주문을 추가한다.(주문1)
     * 음식이 준비되어 / 주문1의 상태를 식사 중 상태로 바꾼다.
     * 추가 주문내용을 받고 / 테이블에 새로운 주문을 추가한다.(주문2)
     * 음식이 준비되어 / 주문2의 상태를 식사 중 상태로 바꾼다.
     * 식사가 끝나면 / 모든 주문의 상태를 계산완료로 바꾼 후, 테이블의 상태를 빈 테이블로 변경한다.
     * 결제를 완료한다.
     */
    @Test
    void 시나리오1(){
        KitchenPosBehaviors.테이블_공석여부_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeEmptyState(false));
        KitchenPosBehaviors.테이블_인원수_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeNumberOfGuests(3));

        Order savedOrder = 주문을_추가하고_확인한다(orderTable1.getId());
        주문의_상태를_변경하고_확인한다(savedOrder.getId(),OrderStatus.MEAL);

        Order savedOrder2 = 주문을_추가하고_확인한다(orderTable1.getId());
        주문의_상태를_변경하고_확인한다(savedOrder2.getId(),OrderStatus.MEAL);

        주문의_상태를_변경하고_확인한다(savedOrder.getId(),OrderStatus.COMPLETION);
        주문의_상태를_변경하고_확인한다(savedOrder2.getId(),OrderStatus.COMPLETION);

        KitchenPosBehaviors.테이블_공석여부_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeEmptyState(true));
        KitchenPosBehaviors.테이블_인원수_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeNumberOfGuests(0));
    }

    private Order 주문을_추가하고_확인한다(Long orderTableId){
        OrderLineItem orderLineItem = OrderLineItemFixtureFactory.createOrderLine(menu.getId(),3);
        Order order = OrderFixtureFactory.createOrder(orderTableId,Lists.newArrayList(orderLineItem));

        ExtractableResponse<Response> createResponse = KitchenPosBehaviors.주문_추가_요청(order);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return createResponse.as(Order.class);
    }

    private void 주문의_상태를_변경하고_확인한다(Long orderTableId, OrderStatus orderStatus){
        ExtractableResponse<Response> response = KitchenPosBehaviors.주문상태변경_요청(orderTableId, OrderFixtureFactory.createParamForUpdateStatus(orderStatus));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }



    /**
     * Scenario : 단체 손님
     * Given
     * 상품, 메뉴그룹, 메뉴, 테이블이 등록되어있다.
     *
     * When / Then
     * 7명의 손님이 와서 4명, 3명으로 나눠 앉고 / 각 테이블의 상태를 비어있지 않은 상태로 변경한다.
     * 테이블1의 주문내용을 받고 / 테이블1 에 주문1을 추가한다.
     * 테이블2의 주문내용을 받고 / 테이블2에 주문2을 추가한다.
     * 음식이 준비되어 / 주문1의 상태를 식사 중 상태로 바꾼다.
     * 음식이 준비되어 / 주문2의 상태를 식사 중 상태로 바꾼다.
     * 식사가 끝나면 / 모든 주문의 상태를 계산완료로, 모든 테이블의 상태를 빈 테이블로 변경한다.
     * 통합 계산 준비를 위해 / 테이블을 그룹으로 지정한다.
     * 결제를 완료되면 / 테이블 그룹을 해제한다.
     */
    @Test
    void 시나리오2(){
        KitchenPosBehaviors.테이블_공석여부_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeEmptyState(false));
        KitchenPosBehaviors.테이블_인원수_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeNumberOfGuests(4));
        KitchenPosBehaviors.테이블_공석여부_변경_요청(orderTable2.getId(),OrderTableFixtureFactory.createParamForChangeEmptyState(false));
        KitchenPosBehaviors.테이블_인원수_변경_요청(orderTable2.getId(),OrderTableFixtureFactory.createParamForChangeNumberOfGuests(3));

        Order savedOrder = 주문을_추가하고_확인한다(orderTable1.getId());
        Order savedOrder2 = 주문을_추가하고_확인한다(orderTable2.getId());

        주문의_상태를_변경하고_확인한다(savedOrder.getId(),OrderStatus.MEAL);
        주문의_상태를_변경하고_확인한다(savedOrder2.getId(),OrderStatus.MEAL);

        주문의_상태를_변경하고_확인한다(savedOrder.getId(),OrderStatus.COMPLETION);
        주문의_상태를_변경하고_확인한다(savedOrder2.getId(),OrderStatus.COMPLETION);

        KitchenPosBehaviors.테이블_공석여부_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeEmptyState(true));
        KitchenPosBehaviors.테이블_인원수_변경_요청(orderTable1.getId(),OrderTableFixtureFactory.createParamForChangeNumberOfGuests(0));

        KitchenPosBehaviors.테이블_공석여부_변경_요청(orderTable2.getId(),OrderTableFixtureFactory.createParamForChangeEmptyState(true));
        KitchenPosBehaviors.테이블_인원수_변경_요청(orderTable2.getId(),OrderTableFixtureFactory.createParamForChangeNumberOfGuests(0));

        TableGroup tableGroup = KitchenPosBehaviors.테이블그룹_생성(TableGroupFixtureFactory.createTableGroup(Lists.newArrayList(orderTable1,orderTable2)));
        KitchenPosBehaviors.테이블그룹_해제_요청(tableGroup.getId());

    }
}
