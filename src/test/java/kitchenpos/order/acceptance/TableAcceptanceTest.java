package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderEmpty;
import kitchenpos.order.domain.OrderGuests;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("주문 테이블 관련 기능 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    /**
     * When 방문한 손님 수를 음수로 하여 주문 테이블 생성을 요청하면
     * Then 주문 테이블을 생성할 수 없다.
     */
    @DisplayName("방문한 손님 수를 음수로 하여 주문 테이블을 생성한다.")
    @Test
    void createTableWithNegativeNumberOfGuests() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성(-5, true);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * When 주문 테이블 생성을 요청하면
     * Then 주문 테이블을 생성할 수 있다.
     */
    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성(0, true);

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * When 주문 테이블 목록을 조회 요청하면
     * Then 주문 테이블 목록을 조회할 있다.
     */
    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        주문_테이블_생성(0, true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_조회();

        // then
        assertThat(response.jsonPath().getList(".", OrderTableResponse.class)).hasSize(1);
    }

    /**
     * When 등록되지 않은 주문 테이블을 빈 주문 테이블로 변경 요청하면
     * Then 빈 주문 테이블로 변경할 수 없다.
     */
    @DisplayName("등록되지 않은 주문 테이블을 빈 주문 테이블로 변경한다.")
    @Test
    void changeEmptyWithNullOrderTable() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_비움_변경(-1L, true);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 등록하고
     * And 메뉴를 등록하고
     * And 주문 테이블을 등록하고
     * And 조리중이거나 식사중인 주문을 등록하고
     * When 빈 주문 테이블로 변경 요청하면
     * Then 빈 주문 테이블로 변경할 수 없다.
     */
    @DisplayName("조리중이거나 식사중인 주문 테이블을 빈 주문 테이블로 변경한다.")
    @Test
    void changeEmptyWithCookingOrEatingOrder() {
        // given
        MenuGroupResponse 양식 = 메뉴_그룹_생성_요청("양식").as(MenuGroupResponse.class);
        MenuResponse 양식_세트 = 메뉴_생성_요청("양식 세트", new BigDecimal(0), 양식.getId(),
                Collections.emptyList()).as(MenuResponse.class);
        OrderTableResponse 주문테이블 = 주문_테이블_생성(2, false).as(OrderTableResponse.class);
        주문_생성(주문테이블.getId(), Arrays.asList(new OrderLineItemRequest(양식_세트.getId(), 1L)))
                .as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_테이블_비움_변경(주문테이블.getId(), true);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 주문 테이블을 등록하고
     * When 빈 주문 테이블로 변경 요청하면
     * Then 빈 주문 테이블로 변경할 수 있다.
     */
    @DisplayName("주문 테이블을 빈 주문 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableResponse 주문테이블 = 주문_테이블_생성(2, false)
                .as(OrderTableResponse.class);

        // when
        OrderTableResponse changed = 주문_테이블_비움_변경(주문테이블.getId(), true)
                .as(OrderTableResponse.class);

        // then
        assertThat(changed.isEmpty()).isTrue();
    }

    /**
     * When 등록되지 않은 주문 테이블을 방문자 손님 수를 변경 요청하면
     * Then 방문자 손님 수를 변경할 수 없다.
     */
    @DisplayName("등록되지 않은 주문 테이블의 방문자 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestsWithNullOrderTable() {
        // when
        ExtractableResponse<Response> response = change_number_of_guests(-1L, 3);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 빈 주문 테이블을 등록하고
     * When 방문자 손님 수를 변경 요청하면
     * Then 방문자 손님 수를 변경할 수 없다.
     */
    @DisplayName("빈 주문 테이블의 방문자 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        // given
        OrderTableResponse 주문테이블 = 주문_테이블_생성(0, true)
                .as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = change_number_of_guests(주문테이블.getId(), 2);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 비어있지 않은 주문 테이블을 등록하고
     * When 방문자 손님 수를 변경 요청하면
     * Then 방문자 손님 수를 변경할 수 있다.
     */
    @DisplayName("비어있지 않은 주문 테이블의 방문자 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableResponse 주문테이블 = 주문_테이블_생성(0, false)
                .as(OrderTableResponse.class);

        // when
        OrderTableResponse changed = change_number_of_guests(주문테이블.getId(), 2)
                .as(OrderTableResponse.class);

        // then
        assertEquals(2, changed.getNumberOfGuests());
    }

    public static ExtractableResponse<Response> 주문_테이블_생성(int numberOfGuests, boolean empty) {
        OrderTableRequest request = OrderTableRequest.of(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_비움_변경(Long orderTableId, boolean empty) {
        OrderEmpty request = OrderEmpty.of(empty);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> change_number_of_guests(Long orderTableId, int numberOfGuests) {
        OrderGuests request = new OrderGuests(numberOfGuests);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all()
                .extract();
    }
}
