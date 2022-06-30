package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    TableRequest 빈_테이블_요청1;
    TableRequest 빈_테이블_요청2;
    TableRequest 테이블_요청;

    @BeforeEach
    public void init() {
        super.init();

        // given
        빈_테이블_요청1 = new TableRequest(0, true);
        빈_테이블_요청2 = new TableRequest(0, true);
        테이블_요청 = new TableRequest(0, false);
    }

    @DisplayName("주문 테이블 생성에 성공한다.")
    @Test
    void 생성() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(빈_테이블_요청1);

        // then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록 조회에 성공한다.")
    @Test
    void 목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 주문_테이블_생성_요청(빈_테이블_요청1);
        ExtractableResponse<Response> createResponse2 = 주문_테이블_생성_요청(테이블_요청);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_응답됨(response);
        주문_테이블_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("주문 테이블을 빈 테이블로 설정하는 데 성공한다.")
    @Test
    void 빈_테이블_설정() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(테이블_요청);

        // when
        ExtractableResponse<Response> response = 빈_테이블_설정_변경_요청(createResponse, true);

        // then
        빈_테이블_설정됨(response);
    }

    @DisplayName("테이블이 단체 지정되어 있으면 빈 테이블 설정에 실패한다.")
    @Test
    void 빈_테이블_설정_예외_단체_지정됨() {
        // given
        ExtractableResponse<Response> createResponse1 = 주문_테이블_생성_요청(빈_테이블_요청1);
        ExtractableResponse<Response> createResponse2 = 주문_테이블_생성_요청(빈_테이블_요청2);
        TableGroupAcceptanceTest.주문_테이블들_단체_지정되어_있음(createResponse1, createResponse2);

        // when
        ExtractableResponse<Response> response = 빈_테이블_설정_변경_요청(createResponse1, true);

        // then
        빈_테이블_설정_실패됨(response);
    }

    @DisplayName("테이블의 주문 상태가 '조리'나 '식사'면 빈 테이블 설정에 실패한다.")
    @Test
    void 빈_테이블_설정_예외_주문_상태_오류() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(테이블_요청);
        주문_생성되어_있음(createResponse);

        // when
        ExtractableResponse<Response> response = 빈_테이블_설정_변경_요청(createResponse, true);

        // then
        빈_테이블_설정_실패됨(response);
    }

    @DisplayName("주문 테이블의 손님 수 변경에 성공한다.")
    @Test
    void 손님_수_변경() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(테이블_요청);

        // when
        ExtractableResponse<Response> response = 손님_수_변경_요청(createResponse, 7);

        // then
        손님_수_변경됨(response);
    }

    @DisplayName("손님 수가 0 미만이면 주문 테이블 손님 수 변경에 실패한다.")
    @Test
    void 손님_수_변경_예외_손님_수_오류() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(테이블_요청);

        // when
        ExtractableResponse<Response> response = 손님_수_변경_요청(createResponse, -5);

        // then
        손님_수_변경_실패됨(response);
    }

    @DisplayName("빈 테이블이면 주문 테이블 손님 수 변경에 실패한다.")
    @Test
    void 손님_수_변경_예외_빈_테이블() {
        // given
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(빈_테이블_요청1);

        // when
        ExtractableResponse<Response> response = 손님_수_변경_요청(createResponse, 7);

        // then
        손님_수_변경_실패됨(response);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성되어_있음(
            int numberOfGuests,
            boolean empty) {
        return 주문_테이블_생성_요청(new TableRequest(numberOfGuests, empty));
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

    public static void 빈_테이블_설정_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 손님_수_변경_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
