package kitchenpos.table.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.util.OrderTableBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static kitchenpos.order.ui.OrderAcceptanceTest.주문_되어있음;
import static kitchenpos.utils.ResponseUtil.getLocationCreatedId;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void createTable() {
        // given
        OrderTableRequest params = new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild();

        // when
        ExtractableResponse<Response> response = 테이블_등록_요청됨(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("테이블 생성시 인원은 0명 이상이어야 한다.")
    @Test
    void createTableNumberOfGuestsGraterThanZero() {
        // given
        OrderTableRequest params = new OrderTableBuilder()
                .withNumberOfGuests(-1)
                .withEmpty(true).requestBuild();

        // when
        ExtractableResponse<Response> response = 테이블_등록_요청됨(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블에 상태값을 변경 가능하다.")
    @Test
    void changeTableEmpty() {
        // given
        long createdId = 테이블_등록_되어_있음(new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild());
        // when
        ExtractableResponse<Response> response = 테이블_상태_변경_요청(createdId, false);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTableResponse.class).isEmpty()).isFalse();
    }

    @DisplayName("테이블의 주문 상태가 요리중, 식사중일때 빈 테이블로 바꿀수 없다.")
    @Test
    void expectedExceptionNotCompleteOrderWhenChangeTableEmpty() {
        // given
        long createdId = 테이블_등록_되어_있음(new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild());

        주문_되어있음(createdId);

        // when
        ExtractableResponse<Response> response = 테이블_상태_변경_요청(createdId, false);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블에 인원을 변경 가능하다.")
    @Test
    void changeNumberOfGuests() {
        // given
        long createdId = 테이블_등록_되어_있음(new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild());
        // when
        ExtractableResponse<Response> response = 테이블_인원_변경_요청(createdId, 5);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("테이블 인원 변경시 0명 이상이어야 한다.")
    @Test
    void changeNumberOfGuestsGraterThanZero() {
        // given
        long createdId = 테이블_등록_되어_있음(new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild());
        // when
        ExtractableResponse<Response> response = 테이블_인원_변경_요청(createdId, -1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static long 테이블_등록_되어_있음(OrderTableRequest params) {
        return getLocationCreatedId(테이블_등록_요청됨(params));
    }

    public static ExtractableResponse<Response> 테이블_상태_변경_요청(long id, boolean empty) {
        Map<String, String> params = new HashMap<>();
        params.put("empty", empty + "");
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                put("/api/tables/{id}/empty", id).
                then().
                log().all().
                extract();
    }

    private ExtractableResponse<Response> 테이블_인원_변경_요청(long id, int numberOfGuests) {
        Map<String, String> params = new HashMap<>();
        params.put("numberOfGuests", numberOfGuests + "");
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                put("/api/tables/{id}/number-of-guests", id).
                then().
                log().all().
                extract();
    }

    private static ExtractableResponse<Response> 테이블_등록_요청됨(OrderTableRequest params) {
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/tables").
                then().
                log().all().
                extract();
    }
}
