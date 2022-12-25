package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.acceptance.OrderAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceTest.주문_생성_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceTest.단체_지정_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 빈_테이블;
    private OrderTableResponse 손님이_있는_테이블;
    private OrderTableResponse 손님이_있는_테이블2;

    @DisplayName("테이블 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> table() {
        return Stream.of(
            dynamicTest("빈 테이블을 등록한다.", () -> {
                ExtractableResponse<Response> response = 테이블_생성_요청(true, 0);

                테이블_생성됨(response);
                빈_테이블 = response.as(OrderTableResponse.class);
            }),
            dynamicTest("손님이 있는 테이블을 등록한다.", () -> {
                ExtractableResponse<Response> response = 테이블_생성_요청(false, 5);
                ExtractableResponse<Response> response2 = 테이블_생성_요청(false, 5);

                테이블_생성됨(response);
                손님이_있는_테이블 = response.as(OrderTableResponse.class);
                테이블_생성됨(response2);
                손님이_있는_테이블2 = response2.as(OrderTableResponse.class);
            }),
            dynamicTest("테이블 목록을 조회한다.", () -> {
                ExtractableResponse<Response> response = 테이블_목록_조회_요청();

                테이블_목록_응답됨(response);
                테이블_목록_응답됨(response, 빈_테이블, 손님이_있는_테이블, 손님이_있는_테이블2);
            }),
            dynamicTest("주문 테이블의 손님 수를 변경한다.", () -> {
                ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(손님이_있는_테이블.getId(), 5);

                테이블_손님_수_변경됨(response);
            }),
            dynamicTest("주문 테이블의 손님 수를 0미만으로 변경한다.", () -> {
                ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(손님이_있는_테이블.getId(), -1);

                테이블_손님_수_변경_실패됨(response);
            }),
            dynamicTest("존재하지 않는 테이블의 손님 수를 변경한다.", () -> {
                ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(Long.MAX_VALUE, 5);

                테이블_손님_수_변경_실패됨(response);
            }),
            dynamicTest("빈 테이블의 손님 수를 변경한다.", () -> {
                ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(빈_테이블.getId(), 5);

                테이블_손님_수_변경_실패됨(response);
            }),
            dynamicTest("빈 테이블을 주문 테이블로 변경한다.", () -> {
                ExtractableResponse<Response> response = 테이블_비움_여부_변경_요청(빈_테이블.getId(), false);

                테이블_비움_여부_변경됨(response);
            }),
            dynamicTest("다시 빈 테이블로 변경한다.", () -> {
                ExtractableResponse<Response> response = 테이블_비움_여부_변경_요청(빈_테이블.getId(), true);

                테이블_비움_여부_변경됨(response);
            }),
            dynamicTest("단체 지정된 주문 테이블의 비움 여부를 변경한다.", () -> {
                단체_지정_생성_요청(손님이_있는_테이블, 손님이_있는_테이블2);

                ExtractableResponse<Response> response = 테이블_비움_여부_변경_요청(손님이_있는_테이블.getId(), true);

                테이블_비움_여부_변경됨(response);
            }),
            dynamicTest("주문이 들어간 테이블의 비움 여부를 변경한다.", () -> {
                OrderTableResponse 주문이_들어간_테이블 = 주문이_들어간_테이블_가져오기();

                ExtractableResponse<Response> response = 테이블_비움_여부_변경_요청(주문이_들어간_테이블.getId(), true);

                테이블_비움_여부_변경_실패됨(response);
            })
        );
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(boolean empty, int numberOfGuests) {
        Map<String, Object> request = new HashMap<>();
        request.put("empty", empty);
        request.put("numberOfGuests", numberOfGuests);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/api/tables")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 테이블_손님_수_변경_요청(Long orderTableId, int numberOfGuests) {
        Map<String, Object> request = new HashMap<>();
        request.put("numberOfGuests", numberOfGuests);

        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 테이블_비움_여부_변경_요청(Long orderTableId, boolean empty) {
        Map<String, Object> request = new HashMap<>();
        request.put("empty", empty);

        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/api/tables/{orderTableId}/empty", orderTableId)
            .then().log().all()
            .extract();
    }

    public static void 테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 테이블_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_목록_응답됨(ExtractableResponse<Response> response, OrderTableResponse... orderTableResponses) {
        List<Long> orderTableIds = response.jsonPath().getList(".", OrderTableResponse.class)
            .stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedIds = Arrays.stream(orderTableResponses)
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        assertThat(orderTableIds).containsExactlyElementsOf(expectedIds);
    }

    public static void 테이블_손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_손님_수_변경_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 테이블_비움_여부_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_비움_여부_변경_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static OrderTableResponse 주문이_들어간_테이블_가져오기() {
        OrderTableResponse 주문이_들어간_테이블 = 테이블_생성_요청(false, 5).as(OrderTableResponse.class);
        MenuResponse 등록된_메뉴 = 메뉴_등록_요청().as(MenuResponse.class);

        주문_생성_요청(주문이_들어간_테이블, 등록된_메뉴);

        return 주문이_들어간_테이블;
    }
}
