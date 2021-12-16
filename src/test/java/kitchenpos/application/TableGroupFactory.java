package kitchenpos.application;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupFactory {

    public static ExtractableResponse<Response> 주문그룹테이블_생성_요청(TableGroupRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/table-groups")
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 주문그룹테이블_삭제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/"+tableGroupId)
                .then().log().all().
                extract();
    }

    public static TableGroupResponse 주문그룹테이블이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(TableGroupResponse.class);
    }
}
