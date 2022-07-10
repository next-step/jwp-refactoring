package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블_변경성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블_변경실패;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블로_변경요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_인원수_변경성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_인원수_변경실패;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.테이블_손님수_변경_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.테이블그룹에속한_주문테이블_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록_요청;

@DisplayName("주문테이블 관련")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    private OrderLineItem 주문항목;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //메뉴
        Product 후라이드 = 상품_등록_요청("후라이드", 16000).as(Product.class);
        MenuGroup 두마리메뉴 = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroup.class);
        MenuProduct menuProduct = new MenuProduct(1);

        //주문
        주문항목 = new OrderLineItem();
        주문항목.setQuantity(1);

    }

    @Test
    void 주문테이블을_등록할_수_있다() {
        ExtractableResponse<Response> 주문테이블_등록_결과 = 주문테이블_등록_요청(true, 5);

        주문테이블_등록성공(주문테이블_등록_결과);
    }

    @Test
    void 주문테이블을_조회할_수_있다() {
        ExtractableResponse<Response> 주문테이블_조회_결과 = 주문테이블_조회_요청();

        주문테이블_조회성공(주문테이블_조회_결과);
    }

    @Test
    void 주문테이블을_빈테이블로_변경할_수_있다() {
        OrderTableResponse 주문테이블 = 주문테이블_등록_요청(true, 5).as(OrderTableResponse.class);
        OrderTableRequest request = new OrderTableRequest(5, true);
        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(주문테이블.getId(), request);

        주문테이블_빈테이블_변경성공(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 존재하지않는_주문테이블을_빈테이블로_변경할_수_없다() {
        long 존재하지않는_주문테이블번호 = 999L;
        OrderTableRequest request = new OrderTableRequest(5, true);
        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(존재하지않는_주문테이블번호, request);

        주문테이블_빈테이블_변경실패(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 테이블그룹에_속한_주문테이블은_빈테이블로_변경할_수_없다() {
        int numberOfGuests = 5;
        boolean empty = true;

        OrderTableResponse 테이블그룹에속한_주문테이블 = 테이블그룹에속한_주문테이블_등록_요청(empty, numberOfGuests).as(OrderTableResponse.class);
        OrderTableResponse 테이블그룹에속한_주문테이블2 = 테이블그룹에속한_주문테이블_등록_요청(empty, numberOfGuests).as(OrderTableResponse.class);
        List<Long> 주문테이블_리스트 = Arrays.asList(테이블그룹에속한_주문테이블.getId(), 테이블그룹에속한_주문테이블2.getId());
        TableGroupResponse 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트).as(TableGroupResponse.class);
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);

        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(테이블그룹_등록_결과.getId(), request);

        주문테이블_빈테이블_변경실패(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 주문테이블의_손님수를_변경할_수_있다() {
        OrderTable 주문테이블 = 주문테이블_등록_요청(false, 5).as(OrderTable.class);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(주문테이블.getId(), 3);

        주문테이블_인원수_변경성공(테이블_손님수_변경_결과, 3);
    }

    @Test
    void 주문테이블의_손님수를_음수로_변경할_수_없다() {
        OrderTable 주문테이블 = 주문테이블_등록_요청(false, 5).as(OrderTable.class);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(주문테이블.getId(), -1);

        주문테이블_인원수_변경실패(테이블_손님수_변경_결과);
    }

    @Test
    void 존재하지않는_주문테이블의_손님수를_변경할_수_없다() {
        OrderTable 존재하지않는_주문테이블 = new OrderTable(999L);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(존재하지않는_주문테이블.getId(), 3);

        주문테이블_인원수_변경실패(테이블_손님수_변경_결과);
    }

    @Test
    void 빈_주문테이블의_손님수를_변경할_수_없다() {
        OrderTableResponse 주문테이블 = 주문테이블_등록_요청(true, 5).as(OrderTableResponse.class);

        ExtractableResponse<Response> 테이블_손님수_변경_결과 = 테이블_손님수_변경_요청(주문테이블.getId(), 3);

        주문테이블_인원수_변경실패(테이블_손님수_변경_결과);
    }

}
