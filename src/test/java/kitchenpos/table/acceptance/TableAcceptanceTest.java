package kitchenpos.table.acceptance;


import static kitchenpos.menu.acceptance.MenuAcceptanceTest.짜장_탕수_메뉴_생성;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_생성됨;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTest.단체지정됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable 빈_테이블_입력 = new OrderTable(null, null, 2,  true);
    private OrderTable 채워진_테이블_입력 = new OrderTable(null, null, 2,  false);
    private OrderTable 빈_테이블;
    private OrderTable 채워진_테이블;


    @DisplayName("주문 테이블 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> table() {
        return Stream.of(
                dynamicTest("빈 테이블을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문테이블_생성_요청(빈_테이블_입력);
                    // then
                    주문테이블_정상_생성됨(response);
                }),

                dynamicTest("채워진 테이블을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 주문테이블_생성_요청(채워진_테이블_입력);
                    // then
                    주문테이블_정상_생성됨(response);
                }),
                dynamicTest("채워진 테이블을 빈테이블로 변환할 수 있다.", () -> {
                    // given
                    채워진_테이블 = 채워진_테이블_생성();
                    채워진_테이블.setEmpty(true);
                    // when
                    ExtractableResponse<Response> response = 주문테이블_변경_요청(채워진_테이블.getId(), 채워진_테이블);
                    // then
                    주문테이블_빈테이블_정상_변경됨(response);}),
                dynamicTest("단체 지정이 되어있는 테이블의 비움 상태를 변경할 수 없다.", () -> {
                    // given
                    OrderTable 빈_테이블_A = 빈_테이블_생성();
                    OrderTable 빈_테이블_B = 빈_테이블_생성();
                    단체지정됨(빈_테이블_A, 빈_테이블_B);
                    // when
                    빈_테이블_A.setEmpty(false);
                    ExtractableResponse<Response> response = 주문테이블_변경_요청(빈_테이블_A.getId(), 빈_테이블_A);
                    // then
                    요청_실패됨(response);

                }),
                dynamicTest("요리중이거나 식사중인 테이블의 비움 상태를 변경할 수 없다.", () -> {
                    // given
                    채워진_테이블 = 채워진_테이블_생성();
                    주문_생성됨(채워진_테이블, 짜장_탕수_메뉴_생성());
                    // when
                    채워진_테이블.setEmpty(true);
                    ExtractableResponse<Response> response = 주문테이블_변경_요청(채워진_테이블.getId(), 채워진_테이블);

                    요청_실패됨(response);
                }),
                dynamicTest("테이블의 손님 수를 변경할 수 있다.", () -> {
                    // given
                    채워진_테이블 = 채워진_테이블_생성();
                    채워진_테이블.setNumberOfGuests(10);
                    // when
                    ExtractableResponse<Response> response = 주문테이블_손님_변경_요청(채워진_테이블.getId(), 채워진_테이블);
                    // then
                    주문테이블_손님수_정상_변경됨(response, 10);}),
                dynamicTest("변경 요청 손님수가 0 미만이면 변경할 수 없다.", () -> {
                    // given
                    채워진_테이블 = 채워진_테이블_생성();
                    채워진_테이블.setNumberOfGuests(-1);
                    // when
                    ExtractableResponse<Response> response = 주문테이블_손님_변경_요청(채워진_테이블.getId(), 채워진_테이블);
                    // then
                    요청_실패됨(response);}),
                dynamicTest("빈 테이블의 손님수는 변경할 수 없다.", () -> {
                    // given
                    빈_테이블 = 빈_테이블_생성();
                    빈_테이블.setNumberOfGuests(10);
                    // when
                    ExtractableResponse<Response> response = 주문테이블_손님_변경_요청(빈_테이블.getId(), 빈_테이블);
                    // then
                    요청_실패됨(response);}),
                dynamicTest("테이블 목록을 조회한다.", () -> {
                    // given
                    OrderTable 채워진_테이블_A = 채워진_테이블_생성();
                    OrderTable 채워진_테이블_B = 채워진_테이블_생성();
                    // when
                    ExtractableResponse<Response> response = 주문테이블_목록_조회_요청();
                    // then
                    주문테이블_목록_정상_조회됨(response, 채워진_테이블_A.getId(), 채워진_테이블_B.getId());})
        );
    }
    public static OrderTable 빈_테이블_생성(){
        OrderTable orderTable = new OrderTable(null, null, 2,  true);
        return 주문테이블_생성_요청(orderTable).as(OrderTable.class);
    }
    public static OrderTable 채워진_테이블_생성(){
        OrderTable orderTable = new OrderTable(null, null, 2,  false);
        return 주문테이블_생성_요청(orderTable).as(OrderTable.class);
    }
    public static OrderTable 빈_테이블_채우기(OrderTable 빈_테이블){
        빈_테이블.setEmpty(false);
        return 주문테이블_변경_요청(빈_테이블.getId(), 빈_테이블).as(OrderTable.class);
    }


    public static ExtractableResponse<Response> 주문테이블_생성_요청(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 주문테이블_변경_요청(Long id, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{id}/empty", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문테이블_손님_변경_요청(Long id, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/{id}/number-of-guests", id)
                .then().log().all()
                .extract();
    }

    private void 주문테이블_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 주문테이블_목록_정상_조회됨(ExtractableResponse<Response> response, Long... ID_목록) {
        List<Long> 조회_결과_ID_목록 = response.jsonPath()
                .getList(".", OrderTable.class)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회_결과_ID_목록).containsAll(Arrays.asList(ID_목록))
        );
    }

    private void 주문테이블_빈테이블_정상_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertTrue(response.as(OrderTable.class).isEmpty());
    }
    private void 주문테이블_손님수_정상_변경됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertEquals(numberOfGuests, response.as(OrderTable.class).getNumberOfGuests());
    }

}
