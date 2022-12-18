package kitchenpos.acceptance;


import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTestFixture {
    public static Long 메뉴_그룹_등록되어_있음(String menuGroup) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", menuGroup);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menu-groups")
                .then().statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().jsonPath().getLong("id");
    }
}
