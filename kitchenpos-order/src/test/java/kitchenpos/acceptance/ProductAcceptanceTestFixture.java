package kitchenpos.acceptance;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTestFixture {
    public static Long 상품_등록_되어_있음(String name, BigDecimal price) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().jsonPath().getLong("id");
    }
}
