package kitchenpos.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 관리한다.")
    @Test
    void manageOrder() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(1L, "false");
        Map<String, Integer> orderLineItem = new HashMap<>();
        orderLineItem.put("menuId", 1);
        orderLineItem.put("quantity", 1);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", 1);
        params.put("orderLineItems", Arrays.asList(orderLineItem));

        // when
        ExtractableResponse<Response> response = OrderAcceptanceTestHelper.주문_생성_요청(params);

        // then
        OrderAcceptanceTestHelper.주문_생성됨(response);

        // when
        ExtractableResponse<Response> getResponse = OrderAcceptanceTestHelper.주문_조회_요청();

        // then
        OrderAcceptanceTestHelper.주문_조회됨(getResponse);
        OrderAcceptanceTestHelper.주문_조회_갯수_예상과_일치(getResponse, 1);

        // given
        Map<String, String> orderStatusParams = new HashMap<>();
        orderStatusParams.put("orderStatus", "MEAL");

        // when
        ExtractableResponse<Response> orderStatusResponse =
            OrderAcceptanceTestHelper.주문_상태_변경_요청(1L, orderStatusParams);

        // then
        OrderAcceptanceTestHelper.주문_상태_변경됨(orderStatusResponse);
        OrderAcceptanceTestHelper.주문_상태_예상과_일치(orderStatusResponse, "MEAL");

    }

    @DisplayName("주문 항목 없을 시 주문 불가능")
    @Test
    void createOrderFailWhenNoItem() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(1L, "false");

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", 1);

        // when
        ExtractableResponse<Response> response = OrderAcceptanceTestHelper.주문_생성_요청(params);

        // then
        OrderAcceptanceTestHelper.주문_생성_실패(response);
    }

    @DisplayName("주문 항목과 메뉴 숫자가 다른 경우(메뉴에 없는 주문 항목) 주문 불가능")
    @Test
    void createOrderFailWhenItemNotExists() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(1L, "false");
        Map<String, Integer> orderLineItem = new HashMap<>();
        orderLineItem.put("menuId", 99);
        orderLineItem.put("quantity", 1);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", 1);
        params.put("orderLineItems", Arrays.asList(orderLineItem));

        // when
        ExtractableResponse<Response> response = OrderAcceptanceTestHelper.주문_생성_요청(params);

        // then
        OrderAcceptanceTestHelper.주문_생성_실패(response);
    }

    @DisplayName("주문 테이블이 없을 시 주문 불가능")
    @Test
    void createOrderFailWhenTableNotExists() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(1L, "false");
        Map<String, Integer> orderLineItem = new HashMap<>();
        orderLineItem.put("menuId", 1);
        orderLineItem.put("quantity", 1);

        Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", 99);
        params.put("orderLineItems", Arrays.asList(orderLineItem));

        // when
        ExtractableResponse<Response> response = OrderAcceptanceTestHelper.주문_생성_요청(params);

        // then
        OrderAcceptanceTestHelper.주문_생성_실패(response);
    }

    @DisplayName("이미 완료상태이면 변경 불가능")
    @Test
    void modifyOrderStatusFailWhenAlreadyCompleted() {
        // given
        TableAcceptanceTestHelper.테이블_빈_테이블_여부_변경되어_있음(1L, "false");

        Map<String, Integer> orderLineItem = new HashMap<>();
        orderLineItem.put("menuId", 1);
        orderLineItem.put("quantity", 1);
        OrderAcceptanceTestHelper.주문_생성되어_있음(Arrays.asList(orderLineItem), 1);
        OrderAcceptanceTestHelper.주문_상태_변경되어_있음(1, "COMPLETION");

        Map<String, String> orderStatusParams = new HashMap<>();
        orderStatusParams.put("orderStatus", "MEAL");

        // when
        ExtractableResponse<Response> orderStatusResponse =
            OrderAcceptanceTestHelper.주문_상태_변경_요청(1L, orderStatusParams);

        // then
        OrderAcceptanceTestHelper.주문_상태_변경_실패(orderStatusResponse);
    }
}