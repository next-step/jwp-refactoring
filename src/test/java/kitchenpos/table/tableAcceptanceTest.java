package kitchenpos.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class tableAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest orderTableRequest;
    private OrderTableRequest changeEmptyRequest;
    private OrderTableRequest changeNumberOfGuestsRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTableRequest = new OrderTableRequest(1, true);
        changeEmptyRequest = new OrderTableRequest(1, false);
        changeNumberOfGuestsRequest = new OrderTableRequest(2, false);
    }

    @DisplayName("DTO와 JPA를 사용하여 주문 테이블을 생성할 수 있다")
    @Test
    void createTest() {

        //when
        ExtractableResponse<Response> response = 주문_테이블_등록_요청(orderTableRequest);

        //then
        정상_등록(response);
        OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @DisplayName("DTO와 JPA를 사용하여 주문 테이블을 조회한다")
    @Test
    void listTest() {
        //given
        주문_테이블_등록_요청(orderTableRequest);

        //when
        ExtractableResponse<Response> response = 주문_테이블_조회_요청();

        //then
        정상_처리(response);
    }

    @DisplayName("DTO와 JPA를 사용하여 주문 테이블 빈 상태를 변경할 수 있다")
    @Test
    void changeEmptyTest() {
        //given
        ExtractableResponse<Response> saveResponse = 주문_테이블_등록_요청(orderTableRequest);
        OrderTableResponse orderResponse = saveResponse.as(OrderTableResponse.class);

        //when
        ExtractableResponse<Response> response = 주문_테이블_빈_상태변경_요청(orderResponse.getId(), changeEmptyRequest);

        //then
        정상_처리(response);
        OrderTableResponse changeResponse = response.as(OrderTableResponse.class);
        assertThat(changeResponse.isEmpty()).isEqualTo(changeEmptyRequest.isEmpty());
    }

    @DisplayName("DTO와 JPA를 사용하여 주문 테이블 고객 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        ExtractableResponse<Response> saveResponse = 주문_테이블_등록_요청(orderTableRequest);
        OrderTableResponse orderResponse = saveResponse.as(OrderTableResponse.class);

        //when
        ExtractableResponse<Response> response = 주문_테이블_방문고객수_변경_요청(orderResponse.getId(), changeNumberOfGuestsRequest);

        //then
        정상_처리(response);
        OrderTableResponse changeResponse = response.as(OrderTableResponse.class);
        assertThat(changeResponse.getNumberOfGuests()).isEqualTo(changeNumberOfGuestsRequest.getNumberOfGuests());
    }


    private ExtractableResponse<Response> 주문_테이블_등록_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables/temp")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables/temp")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_상태변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/empty/temp",orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_방문고객수_변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .body(orderTableRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/tables/{orderTableId}/number-of-guests/temp",orderTableId)
                .then().log().all()
                .extract();
    }

}
