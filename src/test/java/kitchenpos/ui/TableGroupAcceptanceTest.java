package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuApiHelper.메뉴_추가하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_상태_변경하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.ProductApiHelper.상품_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.빈테이블_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.유휴테이블_여부_설정하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableGroupApiHelper.단체_테이블_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableGroupApiHelper.단체_테이블_삭제하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableGroupAssertionHelper.단체_테이블_등록되어있음;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableGroupAssertionHelper.단체_테이블_에러발생;
import static kitchenpos.ui.OrderAcceptanceTest.먹는중;
import static kitchenpos.ui.TableAcceptanceTest.사용중;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.helper.AcceptanceApiHelper.TableApiHelper;
import kitchenpos.helper.AcceptanceApiHelper.TableGroupApiHelper;
import kitchenpos.helper.AcceptanceAssertionHelper.TableGroupAssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 빈테이블_1;
    private OrderTableResponse 빈테이블_2;
    private OrderTableResponse 사용중인테이블;
    private MenuResponse 양념두마리_메뉴;
    private OrderLineItemDTO 주문;

    @BeforeEach
    public void init() {
        테이블_설정하기();
        메뉴_설정하기();
        주문_설정하기();
    }

    private void 주문_설정하기() {
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

        양념두마리_메뉴 = 메뉴_추가하기("양념두마리", 25000, 두마리메뉴.getId(), Arrays.asList(양념_한마리))
            .as(MenuResponse.class);
    }

    private void 테이블_설정하기() {
        빈테이블_1 = 빈테이블_생성하기().as(OrderTableResponse.class);
        빈테이블_2 = 빈테이블_생성하기().as(OrderTableResponse.class);
        사용중인테이블 = 빈테이블_생성하기().as(OrderTableResponse.class);

        유휴테이블_여부_설정하기(사용중, 사용중인테이블.getId());
        TableApiHelper.테이블_손님_인원_설정하기(2, 사용중인테이블.getId());
    }

    /**
     * background
        * given : 빈테이블 2개를 생성하고
     * when : 테이블 2개를 단체로 설정시
     * then : 정상적으로 등록된다.
     */
    @Test
    public void 테이블그룹_생성하기_테스트() {
        //given
        OrderTableRequest 빈테이블_1_request = getOrderTableRequestFromResponse(빈테이블_1);
        OrderTableRequest 빈테이블_2_request = getOrderTableRequestFromResponse(빈테이블_2);

        //when
        ExtractableResponse<Response> 단체_테이블_등록하기_response = 단체_테이블_등록하기(
            Arrays.asList(빈테이블_1_request, 빈테이블_2_request));

        //then
        단체_테이블_등록되어있음(단체_테이블_등록하기_response);
    }

    /**
     * background
        * given : 빈테이블 2개를 생성하고
     * given : 테이블 2개를 단체로 설정하고
     * when : 단체테이블 삭제시
     * then : 정상적으로 삭제된다.
     */
    @Test
    public void 테이블그룹_삭제하기_테스트() {
        //given
        OrderTableRequest 빈테이블_1_request = getOrderTableRequestFromResponse(빈테이블_1);
        OrderTableRequest 빈테이블_2_request = getOrderTableRequestFromResponse(빈테이블_2);

        TableGroupResponse 단체테이블 = 단체_테이블_등록하기(Arrays.asList(빈테이블_1_request, 빈테이블_2_request)).as(
            TableGroupResponse.class);

        //when
        ExtractableResponse<Response> 단체_테이블_삭제하기_response = TableGroupApiHelper.단체_테이블_삭제하기(
            단체테이블.getId());

        //then
        TableGroupAssertionHelper.단체_테이블_삭제됨(단체_테이블_삭제하기_response);
    }

    /**
     * background
        * given : 빈테이블 2개를 생성하고
     * given : 사용중인 테이블 1개를 생성후
     * when : 테이블 3개를 단체로 설정시
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블그룹_사용중인테이블있을시_에러발생() {
        //given
        OrderTableRequest 빈테이블_1_request = getOrderTableRequestFromResponse(빈테이블_1);
        OrderTableRequest 빈테이블_2_request = getOrderTableRequestFromResponse(빈테이블_2);
        OrderTableRequest 사용중인테이블_request = getOrderTableRequestFromResponse(사용중인테이블);

        //when
        ExtractableResponse<Response> 단체_테이블_등록하기_response = 단체_테이블_등록하기(
            Arrays.asList(빈테이블_1_request, 빈테이블_2_request, 사용중인테이블_request));

        //then
        단체_테이블_에러발생(단체_테이블_등록하기_response);
    }

    /**
     * background
        * given : 빈테이블 1개를 생성하고
     * when : 테이블 1개를 단체로 설정시
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블그룹_1개이하테이블_등록시_에러발생() {
        //given
        OrderTableRequest 빈테이블_1_request = getOrderTableRequestFromResponse(빈테이블_1);

        //when
        ExtractableResponse<Response> 단체_테이블_등록하기_response = 단체_테이블_등록하기(
            Arrays.asList(빈테이블_1_request));

        //then
        단체_테이블_에러발생(단체_테이블_등록하기_response);
    }

    /**
     * given : 없는 테이블 2개 생성후
     * when : 없는 테이블을 단체로 설정시
     * then : 에러가 발생한다
     */
    @Test
    public void 테이블그룹_없는테이블_등록시_에러발생() {
        //given
        OrderTableRequest 없는테이블_1 = new OrderTableRequest();
        OrderTableRequest 없는테이블_2 = new OrderTableRequest();

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
    public void 테이블그룹_다먹지않은테이블_삭제시_에러발생() {

        //given
        OrderTableRequest 빈테이블_1_request = getOrderTableRequestFromResponse(빈테이블_1);
        OrderTableRequest 빈테이블_2_request = getOrderTableRequestFromResponse(빈테이블_2);

        TableGroupResponse 단체테이블 = 단체_테이블_등록하기(Arrays.asList(빈테이블_1_request, 빈테이블_2_request)).as(
            TableGroupResponse.class);
        주문_생성하기(빈테이블_1.getId(), Arrays.asList(주문)).as(OrderResponse.class);
        주문_생성하기(빈테이블_2.getId(), Arrays.asList(주문)).as(OrderResponse.class);
        주문_상태_변경하기(먹는중, 빈테이블_1.getId());

        //when
        ExtractableResponse<Response> 단체_테이블_삭제하기_response = 단체_테이블_삭제하기(단체테이블.getId());

        //then
        단체_테이블_에러발생(단체_테이블_삭제하기_response);
    }

    private OrderTableRequest getOrderTableRequestFromResponse(
        OrderTableResponse orderTableResponse) {
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.setNumberOfGuests(orderTableResponse.getNumberOfGuests());
        orderTableRequest.setEmpty(orderTableResponse.isEmpty());
        orderTableRequest.setId(orderTableResponse.getId());
        return orderTableRequest;
    }
}