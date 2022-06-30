package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_리스트_조회하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_추가하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.MenuAssertionHelper.메뉴_등록_에러발생;
import static kitchenpos.helper.AcceptanceAssertionHelper.MenuAssertionHelper.메뉴_등록되어있음;
import static kitchenpos.helper.AcceptanceAssertionHelper.MenuAssertionHelper.메뉴_리스트_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.dto.MenuProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class MenuAcceptanceTest extends AcceptanceTest {


    /**
     * 테이블 테이블_1 : 사용중 , 2명 테이블_2 : 사용중 , 3명 빈테이블_1 : 미사용중, 0명 빈테이블_2 : 미사용중, 0명 메뉴그룹 두마리메뉴 세마리메뉴
     * 반반메뉴 상품 후라이드 : 17000원 양념 : 15000원 메뉴 양념두마리메뉴 : 2222원, 두마리메뉴(그룹명), 양념한마리 양념세마리메뉴 : 3333원,
     * 세마리메뉴(그룹명), 양념세마리 반반메뉴 : 1111원, 반반메뉴(그룹명), 양념한마리&후라이드한마리 주문 양념두마리메뉴,2개
     */
    @BeforeEach
    public void init() {
        super.init();
    }

    /**
     * Background given : 후라이드, 양념 상품을 저장하고 given : 메뉴그룹을 저장하고 given : 메뉴 정보를 구성한뒤 when : 메뉴를 저장하면
     * then : 정상적으로 저장된다.
     */
    @Test
    public void 메뉴_추가하기_테스트() {
        //given
        MenuProductDTO 양념_한마리 = new MenuProductDTO();
        양념_한마리.setProductId(양념.getId()); //양념
        양념_한마리.setQuantity(1L);

        MenuProductDTO 후라이드_한마리 = new MenuProductDTO();
        후라이드_한마리.setProductId(후라이드.getId()); //후라이드
        후라이드_한마리.setQuantity(1L);

        //when
        ExtractableResponse<Response> 메뉴_추가하기_response = 메뉴_추가하기("양념후라이드", 30000, 두마리메뉴.getId(),
            Arrays.asList(양념_한마리, 후라이드_한마리));

        //then
        메뉴_등록되어있음(메뉴_추가하기_response, "양념후라이드");
    }

    /**
     * Background given : 후라이드, 양념 상품을 저장하고 given : 메뉴그룹을 저장하고 given : 메뉴 정보를 저장한뒤 양념두마리메뉴 : 2222원,
     * 두마리메뉴(그룹명), 양념한마리 양념세마리메뉴 : 3333원, 세마리메뉴(그룹명), 양념세마리 양념후라이드메뉴 : 1111원, 반반메뉴(그룹명),
     * 양념한마리&후라이드한마리 when : 메뉴 리스트를 조회하면 then : 정상적으로 조회된다.
     */
    @Test
    public void 메뉴_리스트_조회하기_테스트() {

        //when
        ExtractableResponse<Response> 메뉴_리스트_조회하기 = 메뉴_리스트_조회하기();

        //then
        메뉴_리스트_조회됨(메뉴_리스트_조회하기, Arrays.asList(양념두마리_메뉴, 양념세마리_메뉴, 반반_메뉴));
    }

    /**
     * Background given : 후라이드, 양념 상품을 저장하고 given : 메뉴그룹을 저장하고 given : 가격이 음수인 메뉴 정보를 구성한뒤 when :
     * 메뉴정보를 저장하면 then : 에러가 발생한다.
     */
    @Test
    public void 메뉴가격_음수_에러발생_테스트() {
        //given
        MenuProductDTO 양념_한마리 = new MenuProductDTO();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(1L);

        //when
        ExtractableResponse<Response> 메뉴_추가하기_response = 메뉴_추가하기("양념세마리", -1, 세마리메뉴.getId(),
            Arrays.asList(양념_한마리));

        //then
        메뉴_등록_에러발생(메뉴_추가하기_response);
    }

    /**
     * Background given : 후라이드, 양념 상품을 저장하고 given : 메뉴그룹을 저장하고 given : 가격이 포함된 상품들의 총 가격보다 큰 메뉴 정보를
     * 구성한뒤 when : 메뉴정보를 저장하면 then : 에러가 발생한다.
     */
    @Test
    public void 메뉴가격_상품들_가격보다_클때_에러발생_테스트() {
        //given
        MenuProductDTO 양념_한마리 = new MenuProductDTO();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(2L);
        //when
        ExtractableResponse<Response> 메뉴_추가하기_response = 메뉴_추가하기("양념세마리",
            양념.getPrice().intValue() * 3, 세마리메뉴.getId(),
            Arrays.asList(양념_한마리));

        //then
        메뉴_등록_에러발생(메뉴_추가하기_response);
    }

}