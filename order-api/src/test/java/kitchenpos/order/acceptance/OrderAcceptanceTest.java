package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.acceptance.MenuGroupAcceptanceStep;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.fixture.MenuGroupTestFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.fixture.OrderLineItemTestFixture;
import kitchenpos.order.fixture.OrderTestFixture;
import kitchenpos.ordertable.acceptance.OrderTableAcceptanceStep;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.fixture.OrderTableTestFixture;
import kitchenpos.product.acceptance.ProductAcceptanceStep;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.fixture.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.acceptance.MenuAcceptanceStep.등록된_메뉴;
import static kitchenpos.menu.fixture.MenuProductTestFixture.*;
import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private MenuGroup 중국집1인메뉴세트그룹;
    private MenuProductRequest 짜장면메뉴상품요청;
    private MenuProductRequest 짬뽕메뉴상품요청;
    private MenuProductRequest 단무지메뉴상품요청;
    private MenuProductRequest 탕수육메뉴상품요청;
    private MenuResponse 짜장면_탕수육_1인_메뉴_세트;
    private MenuResponse 짬뽕_탕수육_1인_메뉴_세트;
    private OrderTableResponse 주문테이블1;
    private OrderTableResponse 주문테이블2;
    private OrderLineItem 짜장면_탕수육_1인_메뉴_세트주문;
    private OrderLineItem 짬뽕_탕수육_1인_메뉴_세트주문;
    private OrderRequest 주문1;
    private OrderRequest 주문2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        중국집1인메뉴세트그룹 = MenuGroupAcceptanceStep.등록된_메뉴_그룹(MenuGroupTestFixture.메뉴그룹("중국집1인메뉴세트그룹")).as(MenuGroup.class);
        Long 짜짱면상품ID = ProductAcceptanceStep.등록된_상품(ProductTestFixture.짜장면요청()).as(ProductResponse.class).getId();
        Long 짬뽕상품ID = ProductAcceptanceStep.등록된_상품(ProductTestFixture.짬뽕요청()).as(ProductResponse.class).getId();
        Long 탕수육상품ID = ProductAcceptanceStep.등록된_상품(ProductTestFixture.탕수육요청()).as(ProductResponse.class).getId();
        Long 단무지상품ID = ProductAcceptanceStep.등록된_상품(ProductTestFixture.단무지요청()).as(ProductResponse.class).getId();
        짜장면메뉴상품요청 = 짜장면메뉴상품요청(짜짱면상품ID);
        탕수육메뉴상품요청 = 탕수육메뉴상품요청(탕수육상품ID);
        짬뽕메뉴상품요청 = 짬뽕메뉴상품요청(짬뽕상품ID);
        단무지메뉴상품요청 = 단무지메뉴상품요청(단무지상품ID);
        List<MenuProductRequest> 짜장면메뉴세트 = Arrays.asList(짜장면메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청);
        List<MenuProductRequest> 짬뽕메뉴세트 = Arrays.asList(짬뽕메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청);
        짜장면_탕수육_1인_메뉴_세트 =
                등록된_메뉴(메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집1인메뉴세트그룹.getId(), 짜장면메뉴세트)).as(MenuResponse.class);
        짬뽕_탕수육_1인_메뉴_세트 =
                등록된_메뉴(메뉴세트요청("짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집1인메뉴세트그룹.getId(), 짬뽕메뉴세트)).as(MenuResponse.class);
        주문테이블1 = OrderTableAcceptanceStep.등록된_주문_테이블(OrderTableTestFixture.주문테이블(null, 10, false)).as(OrderTableResponse.class);
        주문테이블2 = OrderTableAcceptanceStep.등록된_주문_테이블(OrderTableTestFixture.주문테이블(null, 20, false)).as(OrderTableResponse.class);
        짜장면_탕수육_1인_메뉴_세트주문 = OrderLineItemTestFixture.주문정보(짜장면_탕수육_1인_메뉴_세트.getId(), 1);
        짬뽕_탕수육_1인_메뉴_세트주문 = OrderLineItemTestFixture.주문정보(짬뽕_탕수육_1인_메뉴_세트.getId(), 1);
        주문1 = OrderTestFixture.주문(주문테이블1.getId(), null, null, OrderLineItemTestFixture.주문정보요청목록(Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문)));
        주문2 = OrderTestFixture.주문(주문테이블2.getId(), null, null, OrderLineItemTestFixture.주문정보요청목록(singletonList(짜장면_탕수육_1인_메뉴_세트주문)));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = OrderAcceptanceStep.주문_생성_요청(주문1);

        // then
        주문_생성됨(response);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> createResponse1 = OrderAcceptanceStep.등록된_주문(주문1);
        ExtractableResponse<Response> createResponse2 = OrderAcceptanceStep.등록된_주문(주문2);

        // when
        ExtractableResponse<Response> response = OrderAcceptanceStep.주문_목록_조회_요청();

        // then
        OrderAcceptanceStep.주문_목록_응답됨(response);
        OrderAcceptanceStep.주문_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderResponse orderResponse = OrderAcceptanceStep.등록된_주문(주문1).as(OrderResponse.class);
        Order order = Order.of(1L,
                Collections.singletonList(짜장면_탕수육_1인_메뉴_세트주문)
        );
        order.changeOrderStatus(OrderStatus.COOKING);

        // when
        ExtractableResponse<Response> response = OrderAcceptanceStep.주문_상태_변경_요청(orderResponse.getId(), order);

        // then
        OrderAcceptanceStep.주문_상태_변경됨(response, order.getOrderStatus());
    }

    private static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }
}
