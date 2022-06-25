package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_리스트_조회하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.ProductAssertionHelper.상품_등록_에러발생;
import static kitchenpos.helper.AcceptanceAssertionHelper.ProductAssertionHelper.상품_등록되어있음;
import static kitchenpos.helper.AcceptanceAssertionHelper.ProductAssertionHelper.상품_리스트_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/test/db/cleanUp.sql"})
class ProductAcceptanceTest extends AcceptanceTest {
    /**
     * when : 상품 정보를 등록하면
     * then : 정상적으로 등록이 되며(201 CREATED), ID가 설정되어 반환된다
    */

    @Test
    public void 상품_등록하기_테스트() {
        //when
        ExtractableResponse<Response> 상품_등록하기_response = 상품_등록하기("간장치킨",18000);

        //then
        상품_등록되어있음(상품_등록하기_response, "간장치킨");
    }

    /**
     * given : 상품 3개가 저장되어있고
     * when : 상품 리스트를 조회했을때
     * then : 정상적으로 조회가 된다.(200 OK)
    */
    @Test
    public void 상품_리스트_조회하기_테스트() {
        //given
        Product 강정치킨 = 상품_등록하기("강정치킨", 18000).as(Product.class);
        Product 카레치킨 = 상품_등록하기("카레치킨", 17000).as(Product.class);
        Product 간장치킨 = 상품_등록하기("간장치킨", 16000).as(Product.class);

        //when
        ExtractableResponse<Response> 상품_리스트_조회하기_response = 상품_리스트_조회하기();

        //then

        상품_리스트_조회됨(상품_리스트_조회하기_response, Arrays.asList(강정치킨, 카레치킨, 간장치킨));
    }

    /**
     * given : 가격이 0이하인 상품 정보가 주어지고
     * when : 상품 등록을 할시
     * then : 에러가 발생한다
    */
    @Test
    public void 상품_등록_에러발생_테스트() {
        //given
        int 가격 = -1;

        //when + then
        ExtractableResponse<Response> 상품_등록하기_response = 상품_등록하기("강정치킨", 가격);

        //then
        상품_등록_에러발생(상품_등록하기_response);
    }
}