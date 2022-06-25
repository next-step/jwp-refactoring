package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.dto.IdOfOrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 관련 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest{

    private static final String TABLE_GROUP_URL = "/v2/api/table-groups";

    OrderTableResponse 테이블1;
    OrderTableResponse 테이블2;

    @TestFactory
    Stream<DynamicTest> menuGroupTest() {
        return Stream.of(
                dynamicTest("테이블들을 단체 지정 할 수 있다.", () -> {
                    테이블1 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
                    테이블2 = 테이블_생성_요청(0, true).as(OrderTableResponse.class);
                    ExtractableResponse<Response> 단체_지정_요청_응답 = 단체_지정_요청(Arrays.asList(테이블1, 테이블2));
                    단체가_지정됨(단체_지정_요청_응답);
                }),
                dynamicTest("테이블들을 단체 해제 할 수 있다.", ()->{
                    ExtractableResponse<Response> 단체_지정_요청_응답 = 단체_지정_요청(Arrays.asList(테이블1, 테이블2));

                    ExtractableResponse<Response> 단체_해제_요청_응답 = 단체_해제_요청(단체_지정_요청_응답);
                    단체가_해제됨(단체_해제_요청_응답);
                })
        );
    }

    public static ExtractableResponse<Response> 단체_지정_요청(List<OrderTableResponse> orderTables) {
        List<IdOfOrderTableRequest> ids = orderTables.stream().
                map(o -> o.getId()).
                map(IdOfOrderTableRequest::new).
                collect(Collectors.toList());
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TableGroupRequest(ids))
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체가_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
