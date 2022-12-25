package kitchenpos.table;

import static kitchenpos.menu.MenuFixture.메뉴_등록;
import static kitchenpos.menu.MenuGroupFixture.메뉴_그룹_등록;
import static kitchenpos.order.OrderFixture.주문;
import static kitchenpos.product.ProductFixture.상품_등록;
import static kitchenpos.table.TableFixture.주문_테이블_목록_조회;
import static kitchenpos.table.TableFixture.주문_테이블_빈_테이블_상태_변경;
import static kitchenpos.table.TableFixture.주문_테이블_추가;
import static kitchenpos.table.TableFixture.주문_테이블의_방문한_손님_수_변경;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableAcceptanceTest extends AcceptanceTest {

    /*
    Feature: 테이블 관리를 합니다.
        Scenario: 주문 테이블을 관리 한다.
            When 주문 테이블을 등록
            Then 주문 테이블 등록됨
            When 주문 테이블 목록을 조회
            Then 주문 테이블 목록 조회됨
            When 주문 테이블 방문한 손님 수 변경
            Then 주문 테이블 방문한 손님 수 변경 됨
            When 주문 테이블 빈 테이블로 변경
            Then 주문 테이블 빈 테이블로 변경 됨
        Background:
            Given: 주문 테이블 생성됨
            And: 상품 등록 되어 있음
            And: 메뉴 등록 되어 있음
            And: 주문 등록 되어 있음
        Scenario: 주문 후 주문 테이블을 빈 테이블로 변경
            When 주문 테이블 빈 테이블로 변경
            Then 에러 발생
     */
    @Test
    void 주문_테이블_관리() {
        //when
        ExtractableResponse<Response> createResponse = 주문_테이블_추가(null, 0, false);
        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<OrderTableResponse> orderTables = 주문_테이블_목록_조회().jsonPath()
            .getList(".", OrderTableResponse.class);
        //then
        assertThat(orderTables)
            .hasSize(1)
            .extracting(OrderTableResponse::getNumberOfGuests, OrderTableResponse::isEmpty)
            .containsExactly(tuple(
                0,
                false
            ));

        //when
        OrderTableResponse numberOfGuestChanged = 주문_테이블의_방문한_손님_수_변경(orderTables.get(0).getId(),
            10)
            .as(OrderTableResponse.class);
        //then
        assertThat(numberOfGuestChanged.getNumberOfGuests()).isEqualTo(10);

        //when
        OrderTableResponse empty = 주문_테이블_빈_테이블_상태_변경(orderTables.get(0).getId(), true)
            .as(OrderTableResponse.class);
        //then
        assertThat(empty.isEmpty()).isTrue();
    }

    @Test
    void 주문_후_주문_테이블을_빈_테이블로_변경() {
        //given
        OrderTableResponse 일번테이블 = 주문_테이블_추가(new OrderTableRequest()).as(OrderTableResponse.class);
        MenuGroupResponse 추천_메뉴 = 메뉴_그룹_등록("추천 메뉴").as(MenuGroupResponse.class);
        ProductResponse 강정치킨 = 상품_등록("강정치킨", new BigDecimal(17_000)).as(ProductResponse.class);
        MenuResponse 더블강정치킨 = 메뉴_등록("더블강정치킨", new BigDecimal(19_000), 추천_메뉴.getId(),
            Collections.singletonList(new MenuProductRequest(강정치킨.getId(), 2L)))
            .as(MenuResponse.class);
        OrderLineItemRequest 주문항목 = new OrderLineItemRequest(더블강정치킨.getId(), 1L);
        주문(일번테이블.getId(), Collections.singletonList(주문항목));

        //when
        ExtractableResponse<Response> 주문_테이블_빈_테이블_상태_변경 = 주문_테이블_빈_테이블_상태_변경(일번테이블.getId(), true);

        //then
        assertThat(주문_테이블_빈_테이블_상태_변경.statusCode())
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
