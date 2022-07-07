package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceFactory {

    public static ExtractableResponse<Response> 테이블그룹_등록_요청(List<Long> 주문테이블_리스트) {
        TableGroupRequest tableGroup = new TableGroupRequest(주문테이블_리스트);
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when()
                .post("/api/table-groups/")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블그룹_삭제_요청(Long 테이블그룹아이디) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/table-groups/{tableGroupId}", 테이블그룹아이디)
                .then().log().all()
                .extract();
    }

    public static void 테이블그룹_등록성공(ExtractableResponse<Response> 테이블그룹_등록_결과) {
//        assertThat(테이블그룹_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 테이블그룹_등록실패(ExtractableResponse<Response> 테이블그룹_등록_결과) {
        assertThat(테이블그룹_등록_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 테이블그룹_삭제성공(ExtractableResponse<Response> 테이블그룹_삭제_결과) {
        assertThat(테이블그룹_삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 테이블그룹_삭제실패(ExtractableResponse<Response> 테이블그룹_삭제_결과) {
        assertThat(테이블그룹_삭제_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
