package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품")
public class ProductAcceptanceTest extends AcceptanceTest {

    ProductRequest 생맥주;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * - 상품관리
     * given 등록할 상품을 생성한다.
     * when 상품을 등록한다.
     * then 상품이 등록됨
     * when 상품목록을 조회한다.
     * then 등록한 상품이 조회 된다.
     * */
    @TestFactory
    @DisplayName("상품을 등록 관리")
    Stream<DynamicTest> manageProduct() {
        return Stream.of(
                dynamicTest("상품을 등록한다.", () -> {
                    //givne
                    생맥주 = new ProductRequest("생맥주", BigDecimal.valueOf(2000));

                    //when
                    ExtractableResponse<Response> createProduct = 상품_등록을_요청(생맥주);
                    //then
                    상품이_등록됨(생맥주, createProduct);
                }),
                dynamicTest("등록한 상품이 조회됨",() -> {
                    //when
                    final ExtractableResponse<Response> searchProducts = 상품_목록을_조회();
                    //then
                    등록한_상품이_조회됨(생맥주, searchProducts);
                })
        );
    }


    private ExtractableResponse<Response> 상품_목록을_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then()
                .log().all()
                .extract();
    }


    @DisplayName("잘못된 가격이면 상품을 등록할 수 없음")
    @ParameterizedTest
    @MethodSource("잘못된_상품가격")
    void noPriceProduct(BigDecimal price) {
        //givne
        ProductRequest 생맥주 = new ProductRequest("생맥주", BigDecimal.valueOf(0));

        //when
        ExtractableResponse<Response> createProduct = 상품_등록을_요청(생맥주);

        //then
        assertThat(createProduct.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private static Stream<Arguments> 잘못된_상품가격() {
        return Stream.of(
                null,
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.ZERO)
        );
    }



    @DisplayName("상품의 이름이 없으면 등록할 수 없음")
    @ParameterizedTest
    @MethodSource("이름이_없는_상품")
    void noNameProduct(String name) {
        //givne
        ProductRequest 이름없는_상품 = new ProductRequest(name, BigDecimal.valueOf(100));

        //when
        ExtractableResponse<Response> createProduct = 상품_등록을_요청(이름없는_상품);

        //then
        assertThat(createProduct.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private static Stream<Arguments> 이름이_없는_상품() {
        return Stream.of(
                null,
                Arguments.of("")
        );
    }


    public static ExtractableResponse<Response> 상품_등록을_요청(ProductRequest productRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then()
                .log().all()
                .extract();
    }

    private void 상품이_등록됨(ProductRequest product, ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
        assertThat(response.jsonPath().getString("name")).isEqualTo(product.getName());
    }

    private void 등록한_상품이_조회됨(ProductRequest product, ExtractableResponse<Response> searchProducts) {
        assertThat(searchProducts.jsonPath().getList(".", ProductResponse.class))
                .extracting("name")
                .contains(product.getName());

    }

}
