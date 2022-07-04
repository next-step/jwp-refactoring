package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 - 식당의 테이블")
public class TableAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> 생성된_주문테이블;
    OrderTableResponse 등록된_주문_테이블;
/*    -- 주문 테이블 관리
    given 주문 테이블 등록할 테이블을 생성한다.
    when 주문가능한 테이블을 요청한다
    then 주문테이블이 생성된다
    when 주문테이블들을 조회한다
    then 등록한 주문 테이블이 조회된다.
    given 등록된 주문테이블로 변경할 방문자 수를  생성한다.
    when 주문 테이블의 방문자를 변경한다
    then 방문자수가 변경이 된다
    when 등록된 주문테이블을 빈테이블로 변경한다
    then 빈테이블로 변경된다.
    */
    @TestFactory
    @DisplayName("주문 테이블 관리")
    Stream<DynamicTest> orderTableManager() {
        return Stream.of(
                dynamicTest("주문테이블을 생성한다.", () -> {
                    //given
                    OrderTableRequest orderTable = new OrderTableRequest(3, false);

                    //when
                    생성된_주문테이블 = 주문가능한_테이블을_요청한다(orderTable);
                    //then
                    주문테이블이_생성된다(생성된_주문테이블);
                })
                ,dynamicTest("생성된 테이블을 조회", () -> {
                    //when
                    final ExtractableResponse<Response> retrievedResponse = 주문_테이블을_조회한다();
                    //then
                    생성된_주문테이블이_조회_된다(생성된_주문테이블, retrievedResponse);
                })
                ,dynamicTest("방문자수를 변경한다.", () -> {
                    //given
                    등록된_주문_테이블 = 생성된_주문테이블.as(OrderTableResponse.class);
                    OrderTableRequest 방문자수변경요청 = new OrderTableRequest(4);
                    //when
                    final ExtractableResponse<Response> updateGuestResponse = 주문_테이블의_방문자를_변경한다(등록된_주문_테이블, 방문자수변경요청);
                    //then
                    방문자가_변경됨(방문자수변경요청.getNumberOfGuests(), updateGuestResponse);
                })
                ,dynamicTest("빈테이블 변경", () -> {
                    //when
                    final ExtractableResponse<Response> updateEmptyTable = 빈테이블로_변경한다(등록된_주문_테이블);
                    //then
                    빈테이블로_변경이_된다(updateEmptyTable);
                })
        );
    }



    public static ExtractableResponse<Response> 주문가능한_테이블을_요청한다 (OrderTableRequest orderTable) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then()
                .log().all()
                .extract();
    }

    private void 주문테이블이_생성된다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
    }

    public static ExtractableResponse<Response> 주문_테이블을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then()
                .log().all()
                .extract();
    }

    private void 생성된_주문테이블이_조회_된다(ExtractableResponse<Response> createResponse, ExtractableResponse<Response> retrievedResponse) {
        assertThat(retrievedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrievedResponse.jsonPath().getList("id", Long.class)).contains(createResponse.as(OrderTable.class).getId());
    }

    private ExtractableResponse<Response> 주문_테이블의_방문자를_변경한다(OrderTableResponse orderTableResponse, OrderTableRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .pathParam("orderTableId", orderTableResponse.getId())
                .when().put("/api/tables/{orderTableId}/number-of-guests")
                .then()
                .log().all()
                .extract();
    }

    private void 방문자가_변경됨(int updateGuestNumber, ExtractableResponse<Response> updateGuestResponse) {
        assertThat(updateGuestResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateGuestResponse.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(updateGuestNumber);
    }


    private ExtractableResponse<Response> 빈테이블로_변경한다(OrderTableResponse orderTableResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("orderTableId", orderTableResponse.getId())
                .when().put("/api/tables/{orderTableId}/empty")
                .then()
                .log().all()
                .extract();
    }

    private void 빈테이블로_변경이_된다(ExtractableResponse<Response> updateEmptyTable) {
        assertThat(updateEmptyTable.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateEmptyTable.as(OrderTableResponse.class).isEmpty()).isTrue();
    }

}
