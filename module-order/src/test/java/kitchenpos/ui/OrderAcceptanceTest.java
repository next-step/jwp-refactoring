package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.오더_리스트_조회하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_상태_변경하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_생성하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_등록되어있음;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_리스트_조회됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_상태_설정됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.OrderAssertionHelper.오더_설정_에러;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class OrderAcceptanceTest extends AcceptanceTest {

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
     * background given : 테이블을 생성하고 테이블_1 : 사용중 , 2명 given : 메뉴를 생성한뒤 given : 주문넣을 메뉴를 설정하고
     * 양념두마리메뉴,2개 when : 주문을 생성하면 then : 정상적으로 생성된다.
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
     * background given : 테이블을 생성하고 given : 메뉴를 생성한뒤 given : 주문넣을 메뉴를 설정하고 given : 주문 3개를 생성 뒤 when
     * : 주문 리스트를 받아오면 then : 정상적으로 조회된다
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
     * background given : 테이블을 생성하고 given : 메뉴를 생성한뒤 given : 주문넣을 메뉴를 설정하고 양념두마리메뉴,2개 given : 주문을
     * 저장하고 when : 주문 상태를 변경시 then : 정상적으로 변경된다.
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
     * background given : 테이블을 생성하고 given : 메뉴를 생성한뒤 given : 주문넣을 메뉴를 설정하고 양념두마리메뉴,2개 given : 주문을
     * 저장하고 when : 주문 상태를 변경시 then : 정상적으로 변경된다.
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
     * when : 없는 메뉴로 주문을 넣을시 then : 에러가 발생한다
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
     * when : 주문이 없이 주문을 넣을시 then : 에러가 발생한다
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
     * when : 등록이 안되있는 테이블에 주문을 넣을시 then : 에러가 발생한다
     */
    @Test
    public void 없는테이블에_주문넣을때_에러발생() {
        //when
        ExtractableResponse<Response> 오더_생성하기_response = 주문_생성하기(-1, Arrays.asList(주문));

        //then
        오더_설정_에러(오더_생성하기_response);
    }

    /**
     * when : 등록이 안되있는 테이블에 주문을 넣을시 then : 에러가 발생한다
     */
    @Test
    public void 비어있는테이블에_주문넣을때_에러발생() {
        //when
        ExtractableResponse<Response> 오더_생성하기_response = 주문_생성하기(빈테이블_1.getId(), Arrays.asList(주문));

        //then
        오더_설정_에러(오더_생성하기_response);
    }
}