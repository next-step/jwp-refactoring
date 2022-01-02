package kitchenpos.order.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.AcceptanceFixture.*;
import static kitchenpos.fixture.MenuAcceptanceFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuAcceptanceFixture.후라이드_치킨_요청;
import static kitchenpos.fixture.OrderAcceptanceFixture.*;
import static kitchenpos.fixture.OrderTableAcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 관련(인수 테스트)")
class OrderAcceptanceTest extends AcceptanceTest {
    OrderTableResponse 한식_테이블_생성됨;
    MenuResponse 후라이드_메뉴_생성됨;
    OrderLineItemRequest 주문_내역;
    OrderRequest 주문_요청;
    OrderResponse 주문_등록됨;

    @BeforeEach
    public void setUp() {
        super.setUp();
        한식_테이블_생성됨 = 주문_테이블_생성_요청(테이블_6인_요청).as(OrderTableResponse.class);
        후라이드_메뉴_생성됨 = 메뉴_생성_요청(후라이드_치킨_요청).as(MenuResponse.class);
        주문_내역 = OrderLineItemRequest.of(후라이드_메뉴_생성됨.getId(), 2);
        주문_요청 = OrderRequest.of(한식_테이블_생성됨.getId(), Lists.newArrayList(주문_내역));
        주문_등록됨 = 주문_생성_요청(주문_요청).as(OrderResponse.class);
    }

    @DisplayName("주문 조회 하기")
    @Test
    void findTest() {
        OrderResponse 주문_등록됨 = 주문_생성_요청(주문_요청).as(OrderResponse.class);
        ExtractableResponse<Response> 주문_조회_요청됨 = 주문_조회_요청();
        OK_응답_잘_받았음(주문_조회_요청됨);
        주문_테이블_조회_확인(주문_조회_요청됨, 주문_등록됨);
    }

    @DisplayName("주문 생성 하기")
    @Test
    void createTest() {
        ExtractableResponse<Response> 주문_생성_요청됨 = 주문_생성_요청(주문_요청);
        CREATE_응답_잘_받음(주문_생성_요청됨);
        주문_등록_확인(주문_생성_요청됨);
    }


    @DisplayName("주문 테이블 없으면 실패함")
    @Test
    void failTest1() {
        주문_요청 = OrderRequest.of(null, Lists.newArrayList(주문_내역));
        ExtractableResponse<Response> actual = 주문_생성_요청(주문_요청);
        BAD_REQUEST_응답_잘_받았음(actual);
    }

    @DisplayName("주문 내역 없으면 실패함")
    @Test
    void failTest2() {
        OrderRequest 주문_요청 = OrderRequest.of(한식_테이블_생성됨.getId(), Lists.newArrayList());
        BAD_REQUEST_응답_잘_받았음(주문_생성_요청(주문_요청));
    }


    @DisplayName("주문 상태 변경 하기")
    @Test
    void changeOrderStatus() {
        OrderStatus 주문_상태_변경_요청 = OrderStatus.COOKING;
        ExtractableResponse<Response> 주문_상태_변경_요청됨 = 주문_상태_변경_요청(OrderRequest.from(주문_상태_변경_요청), 주문_등록됨.getId());
        OK_응답_잘_받았음(주문_상태_변경_요청됨);
        주문_상태_변경_확인(주문_상태_변경_요청됨, 주문_상태_변경_요청);
    }

    @DisplayName("결제 완료 주문 변경 시도하면 실패함")
    @Test
    void failTest3() {
        주문_상태_변경_요청(OrderRequest.from(OrderStatus.COMPLETION), 주문_등록됨.getId());
        ExtractableResponse<Response> 주문_상태_변경_요청됨 = 주문_상태_변경_요청(OrderRequest.from(OrderStatus.COOKING), 주문_등록됨.getId());
        BAD_REQUEST_응답_잘_받았음(주문_상태_변경_요청됨);
    }

    @DisplayName("주문 테이블 비우기 요청")
    @Test
    void changeEmpty() {
        OrderTableResponse 한식_테이블_생성됨 = 주문_테이블_생성_요청(테이블_6인_요청).as(OrderTableResponse.class);
        ExtractableResponse<Response> 주문_테이블_비우기_요청됨 = 주문_테이블_비우기_요청(한식_테이블_생성됨.getId());
        OK_응답_잘_받았음(주문_테이블_비우기_요청됨);
        주문_테이블_비우기_확인(주문_테이블_비우기_요청됨);
    }

    private void 주문_등록_확인(ExtractableResponse<Response> actual) {
        OrderResponse response = actual.as(OrderResponse.class);
        assertThat(response).isNotNull();
    }

    private void 주문_테이블_조회_확인(ExtractableResponse<Response> actual, OrderResponse... expected) {
        List<Long> expectedIds = Arrays.stream(expected)
            .map(OrderResponse::getId)
            .collect(Collectors.toList());

        List<Long> response = actual.jsonPath()
            .getList(".", OrderResponse.class)
            .stream()
            .map(OrderResponse::getId)
            .collect(Collectors.toList());

        assertThat(response).containsAll(expectedIds);
    }

    private void 주문_상태_변경_확인(ExtractableResponse<Response> actual, OrderStatus orderStatus) {
        OrderResponse response = actual.as(OrderResponse.class);
        assertThat(response.getOrderStatus()).isEqualTo(orderStatus);
    }

    private void 주문_테이블_비우기_확인(ExtractableResponse<Response> actual) {
        final OrderTableResponse response = actual.as(OrderTableResponse.class);
        assertThat(response.isEmpty()).isTrue();
    }
}
