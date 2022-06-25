package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_추가하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_상태_변경하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.유휴테이블_여부_설정하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.테이블_손님_인원_설정하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableGroupApiHelper.단체_테이블_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableGroupApiHelper.단체_테이블_삭제하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableGroupAssertionHelper.단체_테이블_등록되어있음;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableGroupAssertionHelper.단체_테이블_에러발생;

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
import kitchenpos.domain.TableGroup;
import kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper;
import kitchenpos.helper.AcceptanceApiHelper.TableApiHelper;
import kitchenpos.helper.AcceptanceApiHelper.TableGroupApiHelper;
import kitchenpos.helper.AcceptanceAssertionHelper.TableGroupAssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/test/db/cleanUp.sql"})
class TableGroupAcceptanceTest  extends AcceptanceTest {

    private OrderTable 빈테이블_1;
    private OrderTable 빈테이블_2;
    private OrderTable 사용중인테이블;
    private Menu 양념두마리_메뉴;
    @BeforeEach
    public void init(){
        테이블_설정하기();
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
    private void 테이블_설정하기() {
        빈테이블_1 = TableApiHelper.빈테이블_생성하기().as(OrderTable.class);
        빈테이블_2 = TableApiHelper.빈테이블_생성하기().as(OrderTable.class);
        사용중인테이블 = TableApiHelper.빈테이블_생성하기().as(OrderTable.class);

        유휴테이블_여부_설정하기("false", 사용중인테이블.getId());
        테이블_손님_인원_설정하기(2, 사용중인테이블.getId());
    }

    /**
      *background
         * given : 빈테이블 2개를 생성하고
     * when : 테이블 2개를 단체로 설정시
     * then : 정상적으로 등록된다.
    */
    @Test
    public void 테이블그룹_생성하기_테스트(){
        //when
        ExtractableResponse<Response> 단체_테이블_등록하기_response = 단체_테이블_등록하기(
            Arrays.asList(빈테이블_1, 빈테이블_2));

        //then
        단체_테이블_등록되어있음(단체_테이블_등록하기_response);
    }

    /**
     *background
         * given : 빈테이블 2개를 생성하고
     * given : 테이블 2개를 단체로 설정하고
     * when : 단체테이블 삭제시
     * then : 정상적으로 삭제된다.
     */
    @Test
    public void 테이블그룹_삭제하기_테스트(){
        //given
        TableGroup 단체테이블 = 단체_테이블_등록하기(Arrays.asList(빈테이블_1, 빈테이블_2)).as(TableGroup.class);

        //when
        ExtractableResponse<Response> 단체_테이블_삭제하기_response = TableGroupApiHelper.단체_테이블_삭제하기(단체테이블.getId());

        //then
        TableGroupAssertionHelper.단체_테이블_삭제됨(단체_테이블_삭제하기_response);
    }

    /**
     *background
         * given : 빈테이블 2개를 생성하고
         * given : 사용중인 테이블 1개를 생성후
     * when : 테이블 3개를 단체로 설정시
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블그룹_사용중인테이블있을시_에러발생(){
        //when
        ExtractableResponse<Response> 단체_테이블_등록하기_response = 단체_테이블_등록하기(
            Arrays.asList(빈테이블_1, 빈테이블_2, 사용중인테이블));

        //then
        단체_테이블_에러발생(단체_테이블_등록하기_response);
    }

    /**
     *background
        * given : 빈테이블 1개를 생성하고
     * when : 테이블 1개를 단체로 설정시
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블그룹_1개이하테이블_등록시_에러발생(){
        //when
        ExtractableResponse<Response> 단체_테이블_등록하기_response = 단체_테이블_등록하기(
            Arrays.asList(빈테이블_1));

        //then
        단체_테이블_에러발생(단체_테이블_등록하기_response);
    }

    /**
     * given : 없는 테이블 2개 생성후
     * when : 없는 테이블을 단체로 설정시
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블그룹_없는테이블_등록시_에러발생(){
        //given
        OrderTable 없는테이블_1 = new OrderTable();
        OrderTable 없는테이블_2 = new OrderTable();

        //when
        ExtractableResponse<Response> 단체_테이블_등록하기_response = 단체_테이블_등록하기(
            Arrays.asList(없는테이블_1, 없는테이블_2));

        //then
        단체_테이블_에러발생(단체_테이블_등록하기_response);
    }

    /**
     * given : 테이블 2개 그룹으로 생성하고
     * given: 한개의 테이블은 주문완료, 한개의 테이블은 주문요리중으로 설정후
     * when : 테이블 그룹 삭제시
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블그룹_다먹지않은테이블_삭제시_에러발생(){
        //given
        TableGroup 단체테이블 = 단체_테이블_등록하기(Arrays.asList(빈테이블_1, 빈테이블_2)).as(TableGroup.class);
        OrderLineItem 주문 = new OrderLineItem();
        주문.setMenuId(양념두마리_메뉴.getId());
        주문.setQuantity(2);

        주문_생성하기(빈테이블_1.getId(), Arrays.asList(주문)).as(Order.class);
        주문_생성하기(빈테이블_2.getId(), Arrays.asList(주문)).as(Order.class);
        주문_상태_변경하기("MEAL", 빈테이블_1.getId());

        //when
        ExtractableResponse<Response> 단체_테이블_삭제하기_response = 단체_테이블_삭제하기(단체테이블.getId());

        //then
        단체_테이블_에러발생(단체_테이블_삭제하기_response);
    }
//       - 제약사항
//       - 등록할 테이블중 사용중인 테이블이 있으면 설정 불가
//       - 등록할 테이블이 1개 이하라면 설정 불가
//       - 등록할 테이블이 저장되어있지 않다면 설정 불가
//   - 단체 계산 후 테이블 그룹 삭제
//     - 제약사항
//       - 그룹의 테이블중 주문상태가 먹고있거나(MEAL), 요리중일때(COOKING) 그룹 테이블 삭제 불가

}