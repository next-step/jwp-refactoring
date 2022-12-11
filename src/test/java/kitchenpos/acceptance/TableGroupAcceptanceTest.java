package kitchenpos.acceptance;


import static kitchenpos.acceptance.MenuAcceptanceTest.짜장_탕수_메뉴_생성;
import static kitchenpos.acceptance.OrderAcceptanceTest.주문_생성됨;
import static kitchenpos.acceptance.TableAcceptanceTest.빈_테이블_생성;
import static kitchenpos.acceptance.TableAcceptanceTest.채워진_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.stream.Stream;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("주문 테이블 단체지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest{
    private OrderTable 빈_주문테이블_A;
    private OrderTable 빈_주문테이블_B;

    private OrderTable 빈_주문테이블_C;
    private OrderTable 빈_주문테이블_D;
    private OrderTable 빈_주문테이블_E;
    private OrderTable 빈_주문테이블_F;
    private OrderTable 채워진_주문테이블;

    @DisplayName("단체지정 통합 테스트")
    @TestFactory
    Stream<DynamicNode> tableGroup() {
        빈_주문테이블_A = 빈_테이블_생성();
        빈_주문테이블_B = 빈_테이블_생성();
        빈_주문테이블_C = 빈_테이블_생성();
        빈_주문테이블_D = 빈_테이블_생성();
        빈_주문테이블_E = 빈_테이블_생성();
        빈_주문테이블_F = 빈_테이블_생성();
        채워진_주문테이블 = 채워진_테이블_생성();

        return Stream.of(
                dynamicTest("단체 지정 한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_주문테이블_A, 빈_주문테이블_B));
                    // then
                    단체지정_정상_생성됨(response);}),
                dynamicTest("테이블이 2개 미만이면 단체지정 할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_주문테이블_C));
                    // then
                    요청_실패됨(response);}),
                dynamicTest("등록되지 않은 테이블이 있으면 단체지정 할 수 없다.", () -> {
                    // given
                    OrderTable 미등록_주문테이블 = new OrderTable(1L, null,4,  true );
                    // when
                    ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_주문테이블_C, 미등록_주문테이블));
                    // then
                    요청_실패됨(response);}),
                dynamicTest("채워진 테이블은 단체지정 할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_주문테이블_C, 채워진_주문테이블));
                    // then
                    요청_실패됨(response);}),
                dynamicTest("이미 단체지정된 테이블은 단체지정 할 수 없다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_주문테이블_A, 채워진_주문테이블));
                    // then
                    요청_실패됨(response);}),
                dynamicTest("단체지정을 취소한다.", () -> {
                    // given
                    TableGroup 주문테이블_단체_지정 = 단체지정됨(빈_주문테이블_C, 빈_주문테이블_D);
                    // when
                    ExtractableResponse<Response> response = 단체지정_해제_요청(주문테이블_단체_지정.getId());
                    // then
                    단체지정_정상_해제됨(response);
                }),
                dynamicTest("조리, 식사중인 주문이 있는 테이블은 단체지정을 취소할 수 없다.", () -> {
                    // given
                    Long 단체지정_ID = 단체지정됨(빈_주문테이블_E, 빈_주문테이블_F).getId();
                    주문_생성됨(빈_주문테이블_E, 짜장_탕수_메뉴_생성());
                    // when
                    ExtractableResponse<Response> response = 단체지정_해제_요청(단체지정_ID);
                    // then
                    요청_실패됨(response);
                })

        );
    }
    private static TableGroup 단체지정_생성(OrderTable...주문테이블){
        return new TableGroup(null, Arrays.asList(주문테이블));
    }
    public static TableGroup 단체지정됨(OrderTable...주문테이블){
        return 단체지정_요청(단체지정_생성(주문테이블)).as(TableGroup.class);
    }


    public static ExtractableResponse<Response> 단체지정_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체지정_해제_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{id}", id)
                .then().log().all()
                .extract();
    }
    private void 단체지정_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    private void 단체지정_정상_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
