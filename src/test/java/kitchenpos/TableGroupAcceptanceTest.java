package kitchenpos;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static kitchenpos.OrderAcceptanceTest.메뉴_등록_요청;
import static kitchenpos.OrderAcceptanceTest.주문_생성_요청;
import static kitchenpos.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문이_들어간_테이블;
    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블4;

    private TableGroup 단체_지정1;
    private TableGroup 단체_지정2;

    @DisplayName("단체 지정 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> tableGroup() {
        return Stream.of(
            dynamicTest("기초 데이터를 추가한다.", () -> {
                테이블1 = 테이블_생성_요청(true, 0).as(OrderTable.class);
                테이블2 = 테이블_생성_요청(true, 0).as(OrderTable.class);
                테이블3 = 테이블_생성_요청(true, 0).as(OrderTable.class);
                테이블4 = 테이블_생성_요청(true, 0).as(OrderTable.class);
            }),
            dynamicTest("단체 지정을 등록한다.", () -> {
                ExtractableResponse<Response> response = 단체_지정_생성_요청(테이블1, 테이블2);

                단체_지정_생성됨(response);
                단체_지정1 = response.as(TableGroup.class);
            }),
            dynamicTest("주문이 들어간 테이블이 포함된 단체 지정을 등록한다.", () -> {
                ExtractableResponse<Response> response = 단체_지정_생성_요청(주문이_들어간_테이블, 테이블3);

                단체_지정_생성_실패됨(response);
            }),
            dynamicTest("1개의 테이블로 단체 지정을 등록한다.", () -> {
                ExtractableResponse<Response> response = 단체_지정_생성_요청(테이블4);

                단체_지정_생성_실패됨(response);
            }),
            dynamicTest("존재하지 않는 테이블이 포함된 단체 지정을 등록한다.", () -> {
                OrderTable 존재하지_않는_테이블 = new OrderTable();
                존재하지_않는_테이블.setId(Long.MAX_VALUE);

                ExtractableResponse<Response> response = 단체_지정_생성_요청(테이블4, 존재하지_않는_테이블);

                단체_지정_생성_실패됨(response);
            }),
            dynamicTest("다른 단체 지정에 포함된 테이블이 포함된 단체 지정을 등록한다.", () -> {
                ExtractableResponse<Response> response = 단체_지정_생성_요청(테이블4, 테이블1);

                단체_지정_생성_실패됨(response);
            }),
            dynamicTest("단체 지정을 등록한다.", () -> {
                ExtractableResponse<Response> response = 단체_지정_생성_요청(테이블3, 테이블4);

                단체_지정_생성됨(response);
                단체_지정2 = response.as(TableGroup.class);
            }),
            dynamicTest("단체 지정을 해지한다.", () -> {
                ExtractableResponse<Response> response = 단체_지정_해지_요청(단체_지정1);

                단체_지정_해지됨(response);
            }),
            dynamicTest("주문이 들어간 테이블이 포함된 단체 지정은 단체 지정을 해지할 수 없다.", () -> {
                주문_생성_요청(테이블3, 메뉴_등록_요청().as(MenuResponse.class));

                ExtractableResponse<Response> response = 단체_지정_해지_요청(단체_지정2);

                단체_지정_해지_실패됨(response);
            })
        );
    }

    public static ExtractableResponse<Response> 단체_지정_생성_요청(OrderTable... orderTables) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTables", Arrays.asList(orderTables));
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해지_요청(TableGroup tableGroup) {
        return RestAssured
            .given().log().all()
            .when().delete("/api/table-groups/{tableGroupId}", tableGroup.getId())
            .then().log().all()
            .extract();
    }

    public static void 단체_지정_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체_지정_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 단체_지정_해지됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 단체_지정_해지_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
