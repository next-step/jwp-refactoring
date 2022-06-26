package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_리스트_조회하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_추가하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.*;
import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.MenuAssertionHelper.메뉴_등록되어있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper;
import kitchenpos.helper.AcceptanceAssertionHelper.MenuAssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;


class MenuAcceptanceTest  extends AcceptanceTest {
    private Product 후라이드;
    private Product 양념;
    private MenuGroup 두마리메뉴;
    private MenuGroup 반반메뉴;
    private MenuGroup 세마리메뉴;
    @BeforeEach
    public void init(){
        후라이드 = 상품_등록하기("후라이드", 17000).as(Product.class);
        양념 = 상품_등록하기("양념", 15000).as(Product.class);

        두마리메뉴 = 메뉴그룹_등록하기("두마리메뉴").as(MenuGroup.class);
        세마리메뉴 = 메뉴그룹_등록하기("세마리메뉴").as(MenuGroup.class);
        반반메뉴 = 메뉴그룹_등록하기("반반메뉴").as(MenuGroup.class);
    }

    /**
     * Background
         * given : 후라이드, 양념 상품을 저장하고
         * given : 메뉴그룹을 저장하고
     * given : 메뉴 정보를 구성한뒤
     * when : 메뉴를 저장하면
     * then : 정상적으로 저장된다.
    */
    @Test
    public void 메뉴_추가하기_테스트() {
        //given
        MenuProduct 양념_한마리 = new MenuProduct();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(1);

        MenuProduct 후라이드_한마리 = new MenuProduct();
        후라이드_한마리.setProductId(후라이드.getId());
        후라이드_한마리.setQuantity(1);

        //when
        ExtractableResponse<Response> 메뉴_추가하기_response = 메뉴_추가하기("양념후라이드", 30000, 두마리메뉴.getId(),
            Arrays.asList(양념_한마리, 후라이드_한마리));

        //then
        메뉴_등록되어있음(메뉴_추가하기_response, "양념후라이드");
    }

    /**
     * Background
         * given : 후라이드, 양념 상품을 저장하고
         * given : 메뉴그룹을 저장하고
     * given : 메뉴 정보를 구성한뒤
     * given : 메뉴 3개를 저장하고
     * when : 메뉴 리스트를 조회하면
     * then : 정상적으로 조회된다.
     */
    @Test
    public void 메뉴_리스트_조회하기_테스트(){
        //given
        MenuProduct 양념_한마리 = new MenuProduct();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(1);

        MenuProduct 후라이드_한마리 = new MenuProduct();
        후라이드_한마리.setProductId(후라이드.getId());
        후라이드_한마리.setQuantity(1);

        MenuProduct 양념_세마리 = new MenuProduct();
        양념_세마리.setProductId(양념.getId());
        양념_세마리.setQuantity(3);

        Menu 양념세마리_메뉴 = 메뉴_추가하기("양념세마리", 40000, 세마리메뉴.getId(), Arrays.asList(양념_세마리)).as(Menu.class);
        Menu 두마리_메뉴 = 메뉴_추가하기("양념후라이드", 30000, 두마리메뉴.getId(), Arrays.asList(양념_한마리, 양념_한마리)).as(
            Menu.class);
        Menu 반반_메뉴 = 메뉴_추가하기("양념후라이드", 30000, 두마리메뉴.getId(), Arrays.asList(양념_한마리, 후라이드_한마리)).as(
            Menu.class);

        //when
        ExtractableResponse<Response> 메뉴_리스트_조회하기 = 메뉴_리스트_조회하기();

        //then
        MenuAssertionHelper.메뉴_리스트_조회됨(메뉴_리스트_조회하기, Arrays.asList(양념세마리_메뉴, 두마리_메뉴, 반반_메뉴));
    }

    /**
     * Background
         * given : 후라이드, 양념 상품을 저장하고
         * given : 메뉴그룹을 저장하고
     * given : 가격이 음수인 메뉴 정보를 구성한뒤
     * when : 메뉴정보를 저장하면
     * then : 에러가 발생한다.
     */
    @Test
    public void 메뉴가격_음수_에러발생_테스트(){
        //given
        MenuProduct 양념_한마리 = new MenuProduct();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(1);

        //when
        ExtractableResponse<Response> 메뉴_추가하기_response = 메뉴_추가하기("양념세마리", -1, 세마리메뉴.getId(),
            Arrays.asList(양념_한마리));

        //then
        MenuAssertionHelper.메뉴_등록_에러발생(메뉴_추가하기_response);
    }

    /**
     * Background
         * given : 후라이드, 양념 상품을 저장하고
         * given : 메뉴그룹을 저장하고
     * given : 가격이 포함된 상품들의 총 가격보다 큰 메뉴 정보를 구성한뒤
     * when : 메뉴정보를 저장하면
     * then : 에러가 발생한다.
     */
    @Test
    public void 메뉴가격_상품들_가격보다_클때_에러발생_테스트(){
        //given
        MenuProduct 양념_한마리 = new MenuProduct();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(2);

        //when
        ExtractableResponse<Response> 메뉴_추가하기_response = 메뉴_추가하기("양념세마리", 양념.getPrice().intValue() * 3, 세마리메뉴.getId(),
            Arrays.asList(양념_한마리));

        //then
        MenuAssertionHelper.메뉴_등록_에러발생(메뉴_추가하기_response);
    }

}