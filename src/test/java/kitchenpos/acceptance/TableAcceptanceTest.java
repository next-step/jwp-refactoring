package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.TableServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableRequest;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    OrderTable orderTable1;
    OrderTable orderTable2;
    TableRequest tableRequest1;
    TableRequest tableRequest2;

    @BeforeEach
    public void init() {
        super.init();

        orderTable1 = TableServiceTest.주문_테이블_생성(null, null, false, 0);
        orderTable2 = TableServiceTest.주문_테이블_생성(null, null, false, 0);
        tableRequest1 = new TableRequest(0, false);
        tableRequest2 = new TableRequest(0, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문_테이블_생성() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(tableRequest1);

        // then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문_테이블_목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 주문_테이블_생성_요청(tableRequest1);
        ExtractableResponse<Response> createResponse2 = 주문_테이블_생성_요청(tableRequest2);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_응답됨(response);
        주문_테이블_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 테이블을 빈 테이블로 설정한다.")
    @Test
    void 빈_테이블_설정() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(tableRequest1);

        // when
        ExtractableResponse<Response> response = 빈_테이블_설정_변경_요청(createResponse, true);

        // then
        빈_테이블_설정됨(response);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void 손님_수_변경() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(tableRequest1);

        // when
        ExtractableResponse<Response> response = 손님_수_변경_요청(createResponse, 7);

        // then
        손님_수_변경됨(response);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성되어_있음(
            Long tableGroupId,
            boolean isEmpty,
            int numberOfGuests) {
        OrderTable orderTable = TableServiceTest.주문_테이블_생성(tableGroupId, null, isEmpty, numberOfGuests);
        return 주문_테이블_생성_요청(orderTable);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(TableRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 빈_테이블_설정_변경_요청(ExtractableResponse<Response> response, boolean empty) {
        String location = response.header("Location");
        TableResponse orderTable = response.as(TableResponse.class);
        TableRequest params = new TableRequest(orderTable.getNumberOfGuests(), empty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location + "/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 손님_수_변경_요청(ExtractableResponse<Response> response, int numberOfGuests) {
        String location = response.header("Location");
        TableResponse orderTable = response.as(TableResponse.class);
        TableRequest params = new TableRequest(numberOfGuests, orderTable.getEmpty());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 주문_테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", TableResponse.class).stream()
                .map(TableResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static void 빈_테이블_설정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
