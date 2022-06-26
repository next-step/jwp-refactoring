package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, false);

        // then
        주문_테이블_생성_검증됨(등록된_주문_테이블);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블1 = 주문_테이블_등록되어_있음(3, false);
        ExtractableResponse<Response> 등록된_주문_테이블2 = 주문_테이블_등록되어_있음(2, false);

        // when
        ExtractableResponse<Response> 주문_테이블_목록 = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_검증됨(주문_테이블_목록);
        주문_테이블_목록_포함됨(주문_테이블_목록, Arrays.asList(등록된_주문_테이블1, 등록된_주문_테이블2));
    }

    @DisplayName("주문 테이블 이용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, true);
        final boolean 테이블_사용중 = false;

        // when
        OrderTable 변경된_주문_테이블 = 주문_테이블_상태_수정_요청(등록된_주문_테이블, 테이블_사용중).as(OrderTable.class);

        // then
        테이블_상태_검증됨(변경된_주문_테이블, 테이블_사용중);
    }

    @DisplayName("주문 테이블 손님 수 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        ExtractableResponse<Response> 등록된_주문_테이블 = 주문_테이블_등록되어_있음(3, false);
        final int 변경된_테이블_손님수 = 5;

        // when
        OrderTable 변경된_주문_테이블 = 주문_테이블_인원_수정_요청(등록된_주문_테이블, 변경된_테이블_손님수).as(OrderTable.class);
        
        // then
        테이블_손님수_검증됨(변경된_주문_테이블, 변경된_테이블_손님수);
    }

    public static ExtractableResponse<Response> 주문_테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return 주문_테이블_생성_요청(orderTable);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .body(orderTable)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_생성_검증됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 주문_테이블_목록_검증됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", OrderTable.class).stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 주문_테이블_인원_수정_요청(ExtractableResponse<Response> response, int numberOfGuests) {
        OrderTable responseOrderTable = response.as(OrderTable.class);
        OrderTable changedOrderTable = new OrderTable(numberOfGuests, responseOrderTable.isEmpty());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests", responseOrderTable.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_상태_수정_요청(ExtractableResponse<Response> response, boolean isEmpty) {
        OrderTable responseOrderTable = response.as(OrderTable.class);
        OrderTable changedOrderTable = new OrderTable(isEmpty);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changedOrderTable)
                .when().put("/api/tables/{orderTableId}/empty", responseOrderTable.getId())
                .then().log().all()
                .extract();
    }

    public static OrderTable 주문_테이블_가져옴(ExtractableResponse<Response> response) {
        return response.as(OrderTable.class);
    }

    public static void 테이블_상태_검증됨(OrderTable 변경된_주문_테이블, boolean 테이블_사용중) {
        assertThat(변경된_주문_테이블.isEmpty()).isEqualTo(테이블_사용중);
    }

    public static void 테이블_손님수_검증됨(OrderTable 변경된_주문_테이블, int 변경된_테이블_손님수) {
        assertThat(변경된_주문_테이블.getNumberOfGuests()).isEqualTo(변경된_테이블_손님수);
    }
}
