package kitchenpos.orderTable.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.orderTable.dto.OrderTableRequest;
import kitchenpos.orderTable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        super.setUp();
    }

    @DisplayName("주문 테이블 생성")
    @Test
    void create() {
        //given
        int numberOfGuests = 1;
        boolean empty = false;
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(numberOfGuests, empty);

        // then
        주문_테이블_생성_요청_됨(response, numberOfGuests, empty);
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        // given
        주문_테이블_생성되어_있음(0, false).as(OrderTableResponse.class);
        주문_테이블_생성되어_있음(0, false).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_요청();

        // then
        주문_테이블_목록_요청_됨(response, 2);
    }

    @DisplayName("빈 주문 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 빈_주문_테이블_상태로_변경_요청(orderTableResponse.getOrderTableId());

        // then
        빈_주문_테이블_상태로_변경_요청됨(response);
    }

    @DisplayName("단체 지정되되어 있으면 안된다.")
    @Test
    void changeEmpty1() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 빈_주문_테이블_상태로_변경_요청(orderTableResponse.getOrderTableId());

        // then
        빈_주문_테이블_상태로_변경_요청됨(response);
    }

    @DisplayName("방문한 손님 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성되어_있음(1, false).as(OrderTableResponse.class);
        int numberOfGuests = 2;

        // when
        ExtractableResponse<Response> response = 방문한_손님_수_변경_요청(orderTableResponse.getOrderTableId(), numberOfGuests);

        // then
        방문한_손님_수_변경_요청_됨(response);
    }

    @DisplayName("변경할 방문 손님 수는 0 이상이어야 한다.")
    @Test
    void changeNumberOfGuests1() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성되어_있음(1, false).as(OrderTableResponse.class);
        int numberOfGuests = -1;

        // when
        ExtractableResponse<Response> response = 방문한_손님_수_변경_요청(orderTableResponse.getOrderTableId(), numberOfGuests);

        // then
        방문한_손님_수_변경_요청_실패됨(response);
    }


    public static ExtractableResponse<Response> 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성되어_있음(int numberOfGuests, boolean empty) {
        return 주문_테이블_생성_요청(numberOfGuests, empty);
    }

    public static void 주문_테이블_생성_요청_됨(ExtractableResponse<Response> response, int numberOfGuests, boolean empty) {
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderTableResponse.getOrderTableId()).isNotNull(),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(numberOfGuests),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(empty)
        );
    }

    private static ExtractableResponse<Response> 주문_테이블_목록_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    private static void 주문_테이블_목록_요청_됨(ExtractableResponse<Response> response, int size) {
        List<OrderTableResponse> orderTableResponses = response.body()
                .jsonPath()
                .getList(".", OrderTableResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderTableResponses).hasSize(size)
        );
    }

    private static ExtractableResponse<Response> 빈_주문_테이블_상태로_변경_요청(Long orderTableId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    private static void 빈_주문_테이블_상태로_변경_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> 방문한_손님_수_변경_요청(Long orderTableId, int numberOfGuests) {
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }

    private static void 방문한_손님_수_변경_요청_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 방문한_손님_수_변경_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
