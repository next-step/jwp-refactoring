package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 지정 관련 인수테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String ORDER_TABLE_GROUP_PATH = "/api/table-groups";

    private ProductResponse 뿌링클;
    private MenuGroupResponse 인기메뉴;
    private MenuResponse 메뉴_뿌링클;

    @BeforeEach
    public void setUp(){
        super.setUp();
        //given
        뿌링클 = ProductAcceptanceTest.상품_등록_되어있음("뿌링클", 27000);
        인기메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록_되어있음("인기 메뉴");
        메뉴_뿌링클 = MenuAcceptanceTest.메뉴_등록_되어있음("뿌링클", 27000, Arrays.asList(뿌링클), 인기메뉴);
    }

    /**
     * Feature:  단체 지정 기능
     *
     *   Background
     *     Given 상품이 등록되어있다.
     *     And   메뉴 그룹이 등록되어있다.
     *     And   메뉴가 등록되어있다.
     *
     *   Scenario: 단체 지정 기능을 관리
     *     Given  빈 테이블들 등록되어있고
     *     When   빈테이블 하나만 단체 지정 등록 요청하면
     *     Then   단체 지정 실패된다.
     *     When   빈 테이블들을 단체 지정 등록 요청하면
     *     Then   단체 지정이 등록된다
     *     When   이미 단체 지정된 테이블들을 단체 지정 등록 요청하면
     *     Then   단체 지정 실패된다.
     *     Given  주문 테이블 등록되어있음
     *     When   주문 테이블이 포함된 단체 지정 등록 요청하면
     *     Then   단체 지정 실패된다.
     *
     *     Given 빈 테이블 등록되어있고
     *     And   단체 지정 등록되어있는
     *     When  단체 지정 해체 요청하면
     *     Then  단체 지정 해제 된다.
     *     Given 빈 테이블 등록되어있고
     *     And   단체 지정 등록되어있고
     *     And   주문 상태가 업데이트 되어있는 테이블을 포함해
     *     When  단체 지정 해체 요청하면
     *     Then  단체 지정 해제에 실패한다.
     *
     * */
    @DisplayName("단체 지정 기능을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageTableGroup() {
        return Stream.of(
                dynamicTest("단체 지정을 등록 한다.", () -> {
                    //given
                    OrderTableResponse emptyTable1 = TableAcceptanceTest.빈_테이블_등록_되어있음(3);
                    OrderTableResponse emptyTable2 = TableAcceptanceTest.빈_테이블_등록_되어있음(5);

                    //when
                    ExtractableResponse<Response> response = 단체_지정_등록_요청(Collections.singletonList(emptyTable1));

                    //then
                    단체_지정_등록_실패됨(response);

                    //when
                    ExtractableResponse<Response> response2 = 단체_지정_등록_요청(Arrays.asList(emptyTable1, emptyTable2));

                    //then
                    단체_지정_등록됨(response2);

                    //when
                    ExtractableResponse<Response> response3 = 단체_지정_등록_요청(Arrays.asList(emptyTable1, emptyTable2));

                    //then
                    단체_지정_등록_실패됨(response3);

                    //given
                    OrderTableResponse orderTable1 = TableAcceptanceTest.주문_테이블_등록_되어있음(3);

                    //when
                    ExtractableResponse<Response> response4 = 단체_지정_등록_요청(Arrays.asList(orderTable1, emptyTable2));

                    //then
                    단체_지정_등록_실패됨(response4);


                }),

                dynamicTest("단체 지정을 해제 한다. ", () -> {
                    //given
                    OrderTableResponse emptyTable1 = TableAcceptanceTest.빈_테이블_등록_되어있음(3);
                    OrderTableResponse emptyTable2 = TableAcceptanceTest.빈_테이블_등록_되어있음(5);
                    ExtractableResponse<Response> created = 단체_지정_등록_요청(Arrays.asList(emptyTable1, emptyTable2));

                    //when
                    ExtractableResponse<Response> response = 단체_지정_해제_요청(created);

                    //then
                    단체_지정_해제됨(response);

                    //given
                    OrderTableResponse emptyTable3 = TableAcceptanceTest.빈_테이블_등록_되어있음(1);
                    OrderTableResponse emptyTable4 = TableAcceptanceTest.빈_테이블_등록_되어있음(2);
                    ExtractableResponse<Response> created2 = 단체_지정_등록_요청(Arrays.asList(emptyTable3, emptyTable4));
                    OrderAcceptanceTest.주문_동록_및_주문_상태_업데이트_되어있음(emptyTable3, Collections.singletonList(메뉴_뿌링클), OrderStatus.MEAL);

                    //when
                    ExtractableResponse<Response> response2 = 단체_지정_해제_요청(created2);

                    //then
                    단체_지정_해제_실패됨(response2);

                })

        );

    }

    private ExtractableResponse<Response> 단체_지정_해제_요청(ExtractableResponse<Response> response) {
        String location = response.header("Location");
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 단체_지정_등록_요청(List<OrderTableResponse> orderTables) {

        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> listParams = new ArrayList<>();

        for (OrderTableResponse emptyTable : orderTables) {
            Map<String, Object> oneParams = new HashMap<>();
            oneParams.put("id", emptyTable.getId());
            listParams.add(oneParams);
        }

        params.put("orderTables", listParams);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(ORDER_TABLE_GROUP_PATH)
                .then().log().all()
                .extract();
    }

    private void 단체_지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }  
    
    private void 단체_지정_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 단체_지정_해제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
