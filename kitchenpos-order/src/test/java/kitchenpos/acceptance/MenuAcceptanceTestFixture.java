package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTestFixture {
    public static Long 메뉴_등록되어_있음(String name, BigDecimal price, Long menuGroup, Long product, int quantity) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);
        request.put("menuGroupId", menuGroup);
        request.put("menuProducts", new HashMap<String, Object>() {{
            put("productId", product);
            put("quantity", quantity);
        }});

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().jsonPath().getLong("id");
    }
}
