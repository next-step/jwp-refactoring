package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.tablegroup.dto.ChaneNumberOfGuestRequest;
import kitchenpos.order.dto.ChangeEmptyRequest;
import kitchenpos.order.dto.TableRequest;
import kitchenpos.order.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class TableRestControllerTest extends AcceptanceSupport {

    private TableRequest 주문테이블_일번;
    private TableRequest 주문테이블_이번;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블_일번 = new TableRequest(3, false);
        주문테이블_이번 = new TableRequest(6, false);
    }

    @Test
    @DisplayName("주문 테이블 등록 할 수 있다.")
    void createOrderTable() {
        // when
        ExtractableResponse<Response> response = 주문테이블을_생성한다(주문테이블_일번);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("주문 테이블 리스트를 받을 수 있다.")
    void getTableList() {
        // given
        TableResponse 주문테이블_일번_응답 = 주문테이블을_생성한다(주문테이블_일번).as(TableResponse.class);
        TableResponse 주문테이블_이번_응답 = 주문테이블을_생성한다(주문테이블_이번).as(TableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문테이블_리스트를_조회해온다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        주문테이블_리스트를_비교한다(response, Arrays.asList(주문테이블_일번_응답.getId(), 주문테이블_이번_응답.getId()));
    }

    @Test
    @DisplayName("주문테이블의 상태를 변경할 수 있다.")
    void changeStatus() {
        // given
        TableResponse 주문테이블_일번_응답 = 주문테이블을_생성한다(주문테이블_일번).as(TableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문테이블_상태_변경을_요청한다(주문테이블_일번_응답.getId(), true);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        주문테이블_변경여부를_확인한다(response, true);
    }

    @Test
    @DisplayName("주문 테이블의 방문한 손님의 수를 바꿀 수 있다.")
    void changeGuestNumber() {
        // given
        TableResponse 주문테이블_일번_응답 = 주문테이블을_생성한다(주문테이블_일번).as(TableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문테이블_손님수_변경을_요청한다(주문테이블_일번_응답.getId(), 10);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        주문테이블_손님수_변경여부를_확인한다(response, 10);
    }

    public static ExtractableResponse<Response> 주문테이블을_생성한다(TableRequest request) {
        return RestAssured
                .given().log()
                .all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/tables")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문테이블_리스트를_조회해온다() {
        return RestAssured
                .given().log()
                .all().when()
                .get("/api/tables")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문테이블_상태_변경을_요청한다(Long id, boolean empty) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ChangeEmptyRequest(empty)).when()
                .put("/api/tables/{id}/empty", id)
                .then().log().all().extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private void 주문테이블_손님수_변경여부를_확인한다(ExtractableResponse<Response> response, int expect) {
        int result = response.jsonPath().getInt("numberOfGuests");

        assertThat(result).isEqualTo(expect);
    }

    private void 주문테이블_리스트를_비교한다(ExtractableResponse<Response> response, List<Long> getId) {
        List<TableResponse> result = response.jsonPath().getList(".", TableResponse.class);
        List<Long> responseId = result.stream().map(TableResponse::getId).collect(Collectors.toList());

        assertThat(responseId).containsAll(getId);
    }

    private void 주문테이블_변경여부를_확인한다(ExtractableResponse<Response> response, boolean expect) {
        boolean result = response.jsonPath().getBoolean("empty");

        assertThat(result).isEqualTo(expect);
    }


    private ExtractableResponse<Response> 주문테이블_손님수_변경을_요청한다(Long id, int numberOfRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ChaneNumberOfGuestRequest(numberOfRequest)).when()
                .put("/api/tables/{id}/number-of-guests", id)
                .then().log().all().extract();
    }

}
