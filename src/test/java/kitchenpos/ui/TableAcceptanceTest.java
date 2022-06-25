package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_추가하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_상태_변경하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.유휴테이블_여부_설정하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.빈테이블_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.테이블_리스트_조회하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.테이블_손님_인원_설정하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_리스트_조회됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_손님수_설정_에러;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_손님수_설정됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_유휴여부_설정_에러;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_유휴여부_설정됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.helper.AcceptanceApiHelper.TableApiHelper;
import kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/test/db/cleanUp.sql"})
class TableAcceptanceTest extends AcceptanceTest {

    private Menu 양념두마리_메뉴;

    @BeforeEach
    public void init(){
        메뉴_설정하기();
    }

    private void 메뉴_설정하기() {
        Product 양념 = 상품_등록하기("양념", 15000).as(Product.class);
        MenuGroup 두마리메뉴 = 메뉴그룹_등록하기("두마리메뉴").as(MenuGroup.class);
        MenuProduct 양념_한마리 = new MenuProduct();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(2);
        양념두마리_메뉴 = 메뉴_추가하기("양념두마리", 25000, 두마리메뉴.getId(), Arrays.asList(양념_한마리)).as(Menu.class);
    }

    /**
     * when : 빈 테이블 저장시
     * then : 성공적으로 저장되고 테이블 정보가 반환된다.
    */
    @Test
    public void 테이블_생성하기_테스트() {
        //when
        ExtractableResponse<Response> 테이블_등록하기_response = 빈테이블_생성하기();

        //then
        TableAssertionHelper.테이블_등록되어있음(테이블_등록하기_response);
    }


    /**
     * background
        * given : 빈테이블 3개 정보를 생성한뒤
     * given : 테이블 3개를 저장후
     * when : 테이블 리스트를 조회하면
     * then : 정상적으로 조회가 된다.
    */
    @Test
    public void 테이블_리스트_조회하기_테스트() {
        //given
        OrderTable 테이블_1_정보 = 빈테이블_생성하기().as(OrderTable.class);
        OrderTable 테이블_2_정보 = 빈테이블_생성하기().as(OrderTable.class);
        OrderTable 테이블_3_정보 = 빈테이블_생성하기().as(OrderTable.class);

        //when
        ExtractableResponse<Response> 테이블_리스트_조회하기_response = 테이블_리스트_조회하기();

        //then
        테이블_리스트_조회됨(테이블_리스트_조회하기_response, Arrays.asList(테이블_1_정보, 테이블_2_정보, 테이블_3_정보));
    }

    /**
     * given : 인원이 없는 테이블 1개 저장후
     * when : 테이블을 빈 테이블로 설정하면
     * then : 정상적으로 빈테이블로 설정된다.
    */
    @Test
    public void 유휴테이블_설정하기_테스트() {
        //given
        String 비어있음 = "true";
        OrderTable 테이블_1_정보 = 빈테이블_생성하기().as(OrderTable.class);

        //when
        ExtractableResponse<Response> 유휴테이블_여부_설정하기_response = 유휴테이블_여부_설정하기(비어있음,
            테이블_1_정보.getId());

        //then
        테이블_유휴여부_설정됨(유휴테이블_여부_설정하기_response, 비어있음);
    }

    /**
     * given : 인원이 없는 테이블 1개 저장후
     * given : 테이블을 사용중으로 변경 후
     * when : 테이블의 손님 수를 설정하면
     * then : 정상적으로 손님 수가 설정된다.
    */
    @Test
    public void 테이블_손님수_설정하기_테스트() {
        //given
        int 손님수 = 4;
        String 사용중 = "false";
        OrderTable 테이블_1_정보 = 빈테이블_생성하기().as(OrderTable.class);
        유휴테이블_여부_설정하기(사용중, 테이블_1_정보.getId());

        //when
        ExtractableResponse<Response> 테이블_손님_인원_설정하기_response = 테이블_손님_인원_설정하기(손님수,
            테이블_1_정보.getId());

        //then
        테이블_손님수_설정됨(테이블_손님_인원_설정하기_response, 손님수);
    }

    /**
     * given : 인원이 없는 테이블 1개 저장후
     * when : 테이블의 손님 수를 설정하면
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블_손님수_설정_빈테이블일때_에러발생_테스트() {
        //given
        int 손님수 = 4;
        OrderTable 테이블_1_정보 = 빈테이블_생성하기().as(OrderTable.class);

        //when
        ExtractableResponse<Response> 테이블_손님_인원_설정하기_response = 테이블_손님_인원_설정하기(손님수,
            테이블_1_정보.getId());

        //then
        테이블_손님수_설정_에러(테이블_손님_인원_설정하기_response);
    }

    /**
     * given : 인원이 없는 테이블 1개 저장후
     * given : 테이블을 사용중으로 변경 후
     * when : 테이블의 손님 수를 음수로 설정하면
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블_손님수_설정_손님_음수일때_에러발생_테스트() {
        //given
        String 사용중 = "false";
        int 손님수 = -1;
        OrderTable 테이블_1_정보 = 빈테이블_생성하기().as(OrderTable.class);
        유휴테이블_여부_설정하기(사용중, 테이블_1_정보.getId());

        //when
        ExtractableResponse<Response> 테이블_손님_인원_설정하기_response = 테이블_손님_인원_설정하기(손님수,
            테이블_1_정보.getId());

        //then
        테이블_손님수_설정_에러(테이블_손님_인원_설정하기_response);
    }

    /**
      * background
         * given : 메뉴를 생성하고
     * given : 주문을 생성하여
     * given : 테이블을 생성 후
     * given : 해당 테이블에 주문을 매핑 후
     * when : 테이블 빈테이블 설정시
     * then : 에러발생
    */
    @Test
    public void 테이블_먹고있거나_요리중에_빈테이블_삭제시_에러발생_테스트(){
        //given(테이블생성)
        OrderTable 테이블_1_정보 = 빈테이블_생성하기().as(OrderTable.class);
        유휴테이블_여부_설정하기("false", 테이블_1_정보.getId());
        //given(주문생성)
        OrderLineItem 주문 = new OrderLineItem();
        주문.setMenuId(양념두마리_메뉴.getId());
        주문.setQuantity(2);
        //given(테이블-주문 매핑)
        주문_생성하기(테이블_1_정보.getId(), Arrays.asList(주문)).as(Order.class);
        주문_상태_변경하기("MEAL", 테이블_1_정보.getId());

        //when
        ExtractableResponse<Response> 유휴테이블_여부_설정하기_response = 유휴테이블_여부_설정하기("true", 테이블_1_정보.getId());

        //then
        테이블_유휴여부_설정_에러(유휴테이블_여부_설정하기_response);
    }

}