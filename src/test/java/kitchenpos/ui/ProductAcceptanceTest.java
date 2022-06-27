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
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;


class ProductAcceptanceTest extends AcceptanceTest {

    /**
     *테이블
         * 테이블_1 : 사용중 , 2명
         * 테이블_2 : 사용중 , 3명
         * 빈테이블_1 : 미사용중, 0명
         * 빈테이블_2 : 미사용중, 0명
     * 메뉴그룹
         * 두마리메뉴
         * 세마리메뉴
         * 반반메뉴
     * 상품
         * 후라이드 : 17000원
         * 양념 : 15000원
     * 메뉴
         * 양념두마리메뉴 : 2222원, 두마리메뉴(그룹명), 양념한마리
         * 양념세마리메뉴 : 3333원, 세마리메뉴(그룹명), 양념세마리
         * 반반메뉴 : 1111원, 반반메뉴(그룹명), 양념한마리&후라이드한마리
     * 주문
         * 양념두마리메뉴,2개
     */

    @BeforeEach
    public void init(){
        super.init();
    }

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
     * background
        * given : 상품 2개가 저장되어있고
             * 후라이드 : 17000원
             * 양념 : 15000원
     * when : 상품 리스트를 조회했을때
     * then : 정상적으로 조회가 된다.(200 OK)
    */
    @Test
    public void 상품_리스트_조회하기_테스트() {
        //when
        ExtractableResponse<Response> 상품_리스트_조회하기_response = 상품_리스트_조회하기();

        //then
        상품_리스트_조회됨(상품_리스트_조회하기_response, Arrays.asList(후라이드, 양념));
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