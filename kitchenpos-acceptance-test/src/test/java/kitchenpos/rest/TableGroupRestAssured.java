package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.resource.UriResource;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import org.springframework.http.MediaType;

public class TableGroupRestAssured {

    public static ExtractableResponse<Response> 주문_테이블_그룹_지정_요청(TableGroupCreateRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(UriResource.주문_테이블_그룹_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_그룹_해지_요청(Long id) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(UriResource.주문_테이블_그룹_API.uri() + "/{tableGroupId}", id)
                .then().log().all()
                .extract();
    }
}
