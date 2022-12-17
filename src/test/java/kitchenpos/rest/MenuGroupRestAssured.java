package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

public class MenuGroupRestAssured {

    public static MenuGroupResponse 메뉴_그룹_등록됨(MenuGroup menuGroup) {
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(menuGroup.getName());
        return 메뉴_그룹_등록_요청(request).as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupCreateRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(UriResource.메뉴_그룹_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(UriResource.메뉴_그룹_API.uri())
                .then().log().all()
                .extract();
    }
}
