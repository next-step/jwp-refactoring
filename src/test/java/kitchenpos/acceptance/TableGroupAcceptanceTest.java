package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.MenuAcceptanceFactory.메뉴_등록_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceFactory.메뉴그룹_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_등록_요청;
import static kitchenpos.acceptance.OrderAcceptanceFactory.주문_상태_변경_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceFactory.상품_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록성공;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록실패;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제성공;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제실패;

@DisplayName("테이블그룹 관련")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 테이블1;
    private OrderTableResponse 테이블2;
    private OrderTableResponse 테이블3;
    private MenuResponse 후라이드메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTableResponse.class);
        테이블2 = 주문테이블_등록_요청(true, 5).as(OrderTableResponse.class);
        테이블3 = 주문테이블_등록_요청(false, 5).as(OrderTableResponse.class);

//        메뉴
        Product 후라이드 = 상품_등록_요청("후라이드", 16000).as(Product.class);
        MenuGroup 두마리메뉴 = 메뉴그룹_등록_요청("두마리메뉴").as(MenuGroup.class);
        MenuProductRequest menuProduct = new MenuProductRequest(후라이드.getId(), 1L);
        후라이드메뉴 = 메뉴_등록_요청(후라이드.getName(), 후라이드.getPriceValue(), 두마리메뉴.getId(), Arrays.asList(menuProduct)).as(MenuResponse.class);
    }

    @Test
    void 테이블그룹을_등록할_수_있다() {
        List<Long> 주문테이블_리스트 = Arrays.asList(테이블1.getId(), 테이블2.getId());

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록성공(테이블그룹_등록_결과);
    }

    @Test
    void 주문테이블이_1개면_테이블그룹을_등록할_수_없다() {
        List<Long> 주문테이블_리스트 = Arrays.asList(테이블1.getId());

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 주문테이블이_존재하지않으면_테이블그룹을_등록할_수_없다() {
        OrderTable 존재하지않는테이블 = new OrderTable();
        List<Long> 주문테이블_리스트 = Arrays.asList(테이블1.getId(), 존재하지않는테이블.getId());

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 비어있지_않은_주문테이블이_존재하면_테이블그룹을_등록할_수_없다() {
        List<Long> 주문테이블_리스트 = Arrays.asList(테이블3.getId());

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 테이블그룹을_삭제할_수_있다() {
        List<Long> 주문테이블_리스트 = Arrays.asList(테이블1.getId(), 테이블2.getId());
        TableGroupResponse 등록된_테이블그룹 = 테이블그룹_등록_요청(주문테이블_리스트).as(TableGroupResponse.class);

        ExtractableResponse<Response> 테이블그룹_삭제_결과 = 테이블그룹_삭제_요청(등록된_테이블그룹.getId());

        테이블그룹_삭제성공(테이블그룹_삭제_결과);
    }

    @ParameterizedTest
    @MethodSource("cookingAndMeal")
    void 주문상태가_요리중이거나_식사중인_테이블그룹은_삭제할_수_없다(OrderStatus status) {
        TableGroupResponse 등록된_테이블그룹 = 테이블그룹_등록_요청(Arrays.asList(테이블1.getId(), 테이블2.getId())).as(TableGroupResponse.class);
        OrderLineItemRequest 주문항목 = new OrderLineItemRequest(후라이드메뉴.getId(), 1);
        OrderResponse 주문 = 주문_등록_요청(테이블1.getId(), Arrays.asList(주문항목)).as(OrderResponse.class);
        주문_상태_변경_요청(주문.getId(), status);

        ExtractableResponse<Response> 테이블그룹_삭제_결과 = 테이블그룹_삭제_요청(등록된_테이블그룹.getId());

        테이블그룹_삭제실패(테이블그룹_삭제_결과);
    }

}
