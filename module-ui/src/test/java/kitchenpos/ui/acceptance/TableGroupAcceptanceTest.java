package kitchenpos.ui.acceptance;

import static kitchenpos.ui.acceptance.TableAcceptanceTest.테이블_생성_요청;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.orders.table.dto.OrderTableResponse;
import kitchenpos.orders.tablegroup.dto.TableGroupRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 관련 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String TABLE_GROUP_URL = "/api/table-groups";

    OrderTableResponse 테이블1;
    OrderTableResponse 테이블2;
    ExtractableResponse<Response> 단체_지정_요청_응답;

    @TestFactory
    Stream<DynamicTest> tableGroupTest() {
        return Stream.of(
                dynamicTest("테이블들을 단체 지정 할 수 있다.", () -> {
                    테이블1 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
                    테이블2 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
                    단체_지정_요청_응답 = 단체_지정_요청(Arrays.asList(테이블1, 테이블2));
                    단체가_지정됨(단체_지정_요청_응답);
                }),
                dynamicTest("하나의 테이블 단체 지정", () -> {
                    OrderTableResponse 하나의_테이블 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
                    ExtractableResponse<Response> 단체_지정_요청 = 단체_지정_요청(Arrays.asList(하나의_테이블));
                    단체가_지정_실패됨(단체_지정_요청);
                }),
                dynamicTest("안빈테이블이 포함된 단체 지정", () -> {
                    OrderTableResponse 안빈_테이블 = 테이블_생성_요청(0, false).as(OrderTableResponse.class);
                    OrderTableResponse 빈_테이블 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
                    ExtractableResponse<Response> 단체_지정_요청 = 단체_지정_요청(Arrays.asList(빈_테이블, 안빈_테이블));
                    단체가_지정_실패됨(단체_지정_요청);
                }),
                dynamicTest("테이블들을 단체 해제 할 수 있다.", () -> {
                    ExtractableResponse<Response> 단체_해제_요청_응답 = 단체_해제_요청(단체_지정_요청_응답);
                    단체가_해제됨(단체_해제_요청_응답);
                })
        );
    }

    public static ExtractableResponse<Response> 단체_지정_요청(List<OrderTableResponse> orderTables) {
        List<Long> ids = orderTables.stream().
                map(o -> o.getId()).
                collect(Collectors.toList());
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(TableGroupRequest.from(ids))
                .when().post(TABLE_GROUP_URL)
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 단체_해제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 단체가_지정됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체가_지정_실패됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 단체가_해제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
