package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성을_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성을_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 생성")
    @Test
    void createTable() {
        ExtractableResponse<Response> response = 테이블_생성을_요청(0, true);

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @DisplayName("주문 테이블 목록을 조회")
    @Test
    void list() {
        테이블_생성을_요청(0, true);

        ExtractableResponse<Response> response = 테이블_목록을_요청();

        assertThat(response.jsonPath().getList(".", OrderTableResponse.class)).hasSize(1);
    }

    @DisplayName("없는 테이블을 공석상태로 변경하면 실패")
    @Test
    void changeEmptyWithNullOrderTable() {
        ExtractableResponse<Response> response = 테이블공석상태_수정을_요청(-1L, 0, true);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("계산완료전 주문테블의 상태를 공석으로 변경하면 실패")
    @Test
    void changeEmptyWithCookingOrEatingOrder() {
        MenuGroupResponse 치킨 = 메뉴그룹_생성을_요청("치킨").as(MenuGroupResponse.class);
        MenuResponse 양념치킨 = 메뉴_생성을_요청("양념치킨", new BigDecimal(0), 치킨.getId(), Collections.emptyList()).as(MenuResponse.class);
        OrderTableResponse 주문테이블 = 테이블_생성을_요청(2, false).as(OrderTableResponse.class);
        주문_생성을_요청(주문테이블.getId(), Arrays.asList(new OrderLineItemRequest(양념치킨.getId(), 1L))).as(OrderResponse.class);

        ExtractableResponse<Response> response = 테이블공석상태_수정을_요청(주문테이블.getId(), 0, true);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("테이블 상태를 공석으로 수정")
    @Test
    void changeEmpty() {
        OrderTableResponse 주문테이블 = 테이블_생성을_요청(2, false).as(OrderTableResponse.class);

        OrderTableResponse changed = 테이블공석상태_수정을_요청(주문테이블.getId(), 0, true).as(OrderTableResponse.class);

        assertTrue(changed.isEmpty());
    }

    @DisplayName("없는 테이블의 손님수를 변경하면 실패")
    @Test
    void changeNumberOfGuestsWithNullOrderTable() {
        ExtractableResponse<Response> response = 테이블손님수_수정을_요청(-1L, 3,true);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("공석 테이블의 손님수를 변경하면 실패")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        OrderTableResponse 주문테이블 = 테이블_생성을_요청(0, true).as(OrderTableResponse.class);

        ExtractableResponse<Response> response = 테이블손님수_수정을_요청(주문테이블.getId(), 2,true);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("비어있지 않은 주문 테이블의 방문자 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTableResponse 주문테이블 = 테이블_생성을_요청(0, false).as(OrderTableResponse.class);

        주문테이블 = 테이블손님수_수정을_요청(주문테이블.getId(), 2,false).as(OrderTableResponse.class);

        assertEquals(2, 주문테이블.getNumberOfGuests());
    }

    public static ExtractableResponse<Response> 테이블_생성을_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_목록을_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블공석상태_수정을_요청(Long orderTableId, int numberOfGuests, boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블손님수_수정을_요청(Long orderTableId, int numberOfGuests,boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all()
                .extract();
    }
}
