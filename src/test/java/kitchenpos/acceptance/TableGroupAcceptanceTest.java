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
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 지정 관련 인수테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String ORDER_TABLE_GROUP_PATH = "/api/table-groups";

    /**
     * Feature:  단체 지정 기능
     *
     *   Scenario: 단체 지정 기능을 관리
     *     Given  빈 테이블들 등록되어있음
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
     *     Given 빈 테이블 등록되어있음
     *     And   단체 지정 등록되어있음
     *     When  단체 지정 해체 요청하면
     *     Then  단체 지정 해제 된다.
     *
     * */
    @DisplayName("단체 지정 기능을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageOrderTable() {
        return Stream.of(
                dynamicTest("단체 지정을 등록 한다.", () -> {
                    //given
                    OrderTable emptyTable1 = TableAcceptanceTest.빈_테이블_등록_되어있음(3);
                    OrderTable emptyTable2 = TableAcceptanceTest.빈_테이블_등록_되어있음(5);

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
                    OrderTable orderTable1 = TableAcceptanceTest.주문_테이블_등록_되어있음(3);

                    //when
                    ExtractableResponse<Response> response4 = 단체_지정_등록_요청(Arrays.asList(orderTable1, emptyTable2));

                    //then
                    단체_지정_등록_실패됨(response4);


                }),

                dynamicTest("단체 지정을 해제 한다. ", () -> {
                    //given
                    OrderTable emptyTable1 = TableAcceptanceTest.빈_테이블_등록_되어있음(3);
                    OrderTable emptyTable2 = TableAcceptanceTest.빈_테이블_등록_되어있음(5);
                    ExtractableResponse<Response> created = 단체_지정_등록_요청(Arrays.asList(emptyTable1, emptyTable2));

                    //when
                    ExtractableResponse<Response> response = 단체_지정_해제_요청(created);

                    //then
                    단체_지정_해제됨(response);
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

    private ExtractableResponse<Response> 단체_지정_등록_요청(List<OrderTable> orderTables) {

        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> listParams = new ArrayList<>();

        for (OrderTable emptyTable : orderTables) {
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
