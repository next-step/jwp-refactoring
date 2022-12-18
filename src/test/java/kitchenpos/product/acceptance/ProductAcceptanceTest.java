package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {

    private Product 피자;
    private Product 파스타;
    private Product 족발;

    @BeforeEach
    public void setUp() {
        super.setUp();

        피자 = ProductRestAssured.from("피자", 32000);
        파스타 = ProductRestAssured.from("파스타", 26000);
        족발 = ProductRestAssured.from("족발", 38000);

        ProductRestAssured.상품_등록_되어_있음(피자);
        ProductRestAssured.상품_등록_되어_있음(파스타);
        ProductRestAssured.상품_등록_되어_있음(족발);
    }

    @DisplayName("상품 생성 요청")
    @Test
    void createProduct() {
        Product 치킨 = ProductRestAssured.from("치킨", 16000);

        ExtractableResponse<Response> response = ProductRestAssured.상품_생성_요청(치킨);

        ProductRestAssured.상품_생성됨(response);
    }

    @DisplayName("상품 생성 - 금액0")
    @Test
    void createProduct_priceIsZero() {
        Product 치킨 = ProductRestAssured.from("치킨", 0);

        ExtractableResponse<Response> response = ProductRestAssured.상품_생성_요청(치킨);

        ProductRestAssured.상품_생성됨(response);
    }

    @DisplayName("상품 생성 예외 발생 확인 - 금액 -1")
    @Test
    void makeExceptionWhenCreateProduct_priceIsMinus() {
        Product 치킨 = ProductRestAssured.from("치킨", -1);

        ExtractableResponse<Response> response = ProductRestAssured.상품_생성_요청(치킨);

        ProductRestAssured.상품_생성_안됨(response);
    }

    @DisplayName("상품 생성 예외 발생 확인 - 이름 null")
    @Test
    void makeExceptionWhenCreateProduct_nameIsNull() {
        Product 치킨 = ProductRestAssured.from(null, -1);

        ExtractableResponse<Response> response = ProductRestAssured.상품_생성_요청(치킨);

        ProductRestAssured.상품_생성_안됨(response);
    }

    @DisplayName("상품 조회")
    @Test
    void showProductList() {
        ExtractableResponse<Response> response = ProductRestAssured.상품_조회_요청();

        ProductRestAssured.상품_조회_목록_응답됨(response);
        ProductRestAssured.상품_조회_목록_포함됨(response, Arrays.asList(피자.getName(), 파스타.getName(), 족발.getName()));
    }
}
