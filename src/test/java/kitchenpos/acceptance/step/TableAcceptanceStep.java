package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.springframework.http.MediaType;

public class TableAcceptanceStep {

    private static final String API_URL = "/api/tables";

    private TableAcceptanceStep() {
    }

    public static OrderTable 주문테이블_생성됨(OrderTable orderTable) {
        return 주문테이블_등록_검증(주문테이블_등록_요청(orderTable));
    }

    public static ExtractableResponse<Response> 주문테이블_등록_요청(OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_목록조회() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 주문테이블_빈테이블_상태_변경_요청(Long orderTableId,
        OrderTable changeOrderTable) {
        return RestAssured
            .given().log().all()
            .body(changeOrderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(API_URL + "/" + orderTableId + "/empty")
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> 주문테이블_방문손님수_변경_요청(Long 주문테이블번호,
        OrderTable 변경요청테이블) {
        return RestAssured
            .given().log().all()
            .body(변경요청테이블)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put(API_URL + "/" + 주문테이블번호 + "/number-of-guests")
            .then().log().all()
            .extract();
    }

    public static OrderTable 주문테이블_등록_검증(ExtractableResponse<Response> 주문테이블_등록_결과) {
        OrderTable 등록된_주문테이블 = 주문테이블_등록_결과.as(OrderTable.class);

        assertThat(등록된_주문테이블.getId()).isNotNull();
        assertThat(등록된_주문테이블.getTableGroupId()).isNull();

        return 등록된_주문테이블;
    }

    public static OrderTable 빈테이블_변경_검증(ExtractableResponse<Response> 빈테이블_변경_결과, boolean 빈테이블유무) {
        OrderTable 변경된_테이블 = 빈테이블_변경_결과.as(OrderTable.class);

        assertThat(변경된_테이블.isEmpty()).isEqualTo(빈테이블유무);

        return 변경된_테이블;
    }

    public static void 주문테이블_목록조회_검증(ExtractableResponse<Response> 주문테이블_목록조회_결과,
        OrderTable 등록된_주문테이블) {
        List<OrderTable> 조회된_메뉴그룹_목록 = 주문테이블_목록조회_결과.as(new TypeRef<List<OrderTable>>() {
        });

        assertThat(조회된_메뉴그룹_목록).contains(등록된_주문테이블);
    }

    public static void 방문한_손님_수_변경_검증(ExtractableResponse<Response> 방문한_손님_수_변경_결과,
        OrderTable 예상_주문테이블) {
        OrderTable 변경된_주문테이블 = 방문한_손님_수_변경_결과.as(OrderTable.class);

        assertThat(변경된_주문테이블.getId()).isEqualTo(예상_주문테이블.getId());
        assertThat(변경된_주문테이블.getNumberOfGuests()).isEqualTo(예상_주문테이블.getNumberOfGuests());
    }

}
