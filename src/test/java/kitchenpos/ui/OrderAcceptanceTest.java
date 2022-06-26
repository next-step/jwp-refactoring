package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_추가하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.오더_리스트_조회하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_상태_변경하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.유휴테이블_여부_설정하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_등록되어있음;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_리스트_조회됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_상태_설정됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_설정_에러;
import static kitchenpos.ui.TableAcceptanceTest.사용중;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.helper.AcceptanceApiHelper.TableApiHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;


class OrderAcceptanceTest extends AcceptanceTest {

    public final static String 먹는중 = "MEAL";
    public static final String 요리중 = "COOKING";

    private OrderTableResponse 테이블_1;
    private OrderTableResponse 테이블_2;
    private OrderTableResponse 빈테이블;
    private MenuResponse 양념두마리_메뉴;
    private OrderLineItemDTO 주문;
    @BeforeEach
    public void init() {
        테이블_설정하기();
        메뉴_설정하기();
        주문_설정하기();
    }

    private void 주문_설정하기(){
        주문 = new OrderLineItemDTO();
        주문.setMenuId(양념두마리_메뉴.getId());
        주문.setQuantity(2L);
    }

    private void 메뉴_설정하기() {
        ProductResponse 양념 = 상품_등록하기("양념", 15000).as(ProductResponse.class);
        MenuGroupResponse 두마리메뉴 = 메뉴그룹_등록하기("두마리메뉴").as(MenuGroupResponse.class);
        MenuProductDTO 양념_한마리 = new MenuProductDTO();
        양념_한마리.setProductId(양념.getId());
        양념_한마리.setQuantity(2L);

        양념두마리_메뉴 = 메뉴_추가하기("양념두마리", 25000, 두마리메뉴.getId(), Arrays.asList(양념_한마리)).as(MenuResponse.class);
    }

    private void 테이블_설정하기() {
        테이블_1 = TableApiHelper.빈테이블_생성하기().as(OrderTableResponse.class);
        테이블_2 = TableApiHelper.빈테이블_생성하기().as(OrderTableResponse.class);
        빈테이블 = TableApiHelper.빈테이블_생성하기().as(OrderTableResponse.class);

        유휴테이블_여부_설정하기(사용중, 테이블_1.getId());
        유휴테이블_여부_설정하기(사용중, 테이블_2.getId());
        TableApiHelper.테이블_손님_인원_설정하기(2, 테이블_1.getId());
        TableApiHelper.테이블_손님_인원_설정하기(3, 테이블_2.getId());
    }


    /**
     * background
        * given : 테이블을 생성하고
        * given : 메뉴를 생성한뒤
        * given : 주문넣을 메뉴를 설정하고
     * when : 주문을 생성하면
     * then : 정상적으로 생성된다.
     */
    @Test
    public void 주문_생성하기_테스트() {
        //when
        ExtractableResponse<Response> 오더_생성하기_response = 주문_생성하기(테이블_1.getId(),
            Arrays.asList(주문));

        //then
        오더_등록되어있음(오더_생성하기_response);
    }


    /**
     * background
         * given : 테이블을 생성하고
         * given : 메뉴를 생성한뒤
         * given : 주문넣을 메뉴를 설정하고
     * given : 주문 3개를 생성 뒤
     * when : 주문 리스트를 받아오면
     * then : 정상적으로 조회된다
     */
    @Test
    public void 주문_리스트_조회하기_테스트() {
        //given
        OrderResponse 주문_1 = 주문_생성하기(테이블_1.getId(), Arrays.asList(주문)).as(OrderResponse.class);
        OrderResponse 주문_2 = 주문_생성하기(테이블_1.getId(), Arrays.asList(주문)).as(OrderResponse.class);
        OrderResponse 주문_3 = 주문_생성하기(테이블_2.getId(), Arrays.asList(주문)).as(OrderResponse.class);

        //when
        ExtractableResponse<Response> 오더_리스트_조회하기_response = 오더_리스트_조회하기();

        //then
        오더_리스트_조회됨(오더_리스트_조회하기_response, Arrays.asList(주문_1, 주문_2, 주문_3));
    }

    /**
     * background
         * given : 테이블을 생성하고
         * given : 메뉴를 생성한뒤
         * given : 주문넣을 메뉴를 설정하고
     * given : 주문을 저장하고
     * when : 주문 상태를 변경시
     * then : 정상적으로 변경된다.
     */
    @Test
    public void 주문_상태_변경하기_MEAL() {
        //given
        OrderResponse 주문_1 = 주문_생성하기(테이블_1.getId(), Arrays.asList(주문)).as(OrderResponse.class);

        //when
        ExtractableResponse<Response> 주문_상태_변경하기_response = 주문_상태_변경하기(먹는중, 주문_1.getId());

        //then
        오더_상태_설정됨(주문_상태_변경하기_response, 먹는중);
    }

    /**
     * background
         * given : 테이블을 생성하고
         * given : 메뉴를 생성한뒤
         * given : 주문넣을 메뉴를 설정하고
     * given : 주문을 저장하고
     * when : 주문 상태를 변경시
     * then : 정상적으로 변경된다.
     */
    @Test
    public void 주문_상태_변경하기_COMPLETION() {
        //given
        OrderResponse 주문_1 = 주문_생성하기(테이블_1.getId(), Arrays.asList(주문)).as(OrderResponse.class);

        //when
        ExtractableResponse<Response> 주문_상태_변경하기_response = 주문_상태_변경하기("COMPLETION", 주문_1.getId());

        //then
        오더_상태_설정됨(주문_상태_변경하기_response, "COMPLETION");
    }

    /**
     * when : 없는 메뉴로 주문을 넣을시
     * then : 에러가 발생한다
     */
    @Test
    public void 없는_메뉴로_주문할때_에러발생() {
        //given
        OrderLineItemDTO 주문 = new OrderLineItemDTO();
        주문.setMenuId(-1L);
        주문.setQuantity(2L);

        //when
        ExtractableResponse<Response> 오더_생성하기_response = 주문_생성하기(테이블_1.getId(),
            Arrays.asList(주문));

        //then
        오더_설정_에러(오더_생성하기_response);
    }

    /**
     * when : 주문이 없이 주문을 넣을시
     * then : 에러가 발생한다
     */
    @Test
    public void 주문이_없을때_에러발생() {
        //when
        ExtractableResponse<Response> 오더_생성하기_response = 주문_생성하기(테이블_1.getId(),
            new ArrayList<>());

        //then
        오더_설정_에러(오더_생성하기_response);
    }

    /**
     * when : 등록이 안되있는 테이블에 주문을 넣을시
     * then : 에러가 발생한다
     */
    @Test
    public void 없는테이블에_주문넣을때_에러발생() {
        //when
        ExtractableResponse<Response> 오더_생성하기_response = 주문_생성하기(-1, Arrays.asList(주문));

        //then
        오더_설정_에러(오더_생성하기_response);
    }

    /**
     * when : 등록이 안되있는 테이블에 주문을 넣을시
     * then : 에러가 발생한다
     */
    @Test
    public void 비어있는테이블에_주문넣을때_에러발생() {
        //when
        ExtractableResponse<Response> 오더_생성하기_response = 주문_생성하기(빈테이블.getId(), Arrays.asList(주문));

        //then
        오더_설정_에러(오더_생성하기_response);
    }
}