package kitchenpos.ordertable.acceptance;

import static kitchenpos.order.acceptance.OrderAcceptanceTestFixture.주문_등록_되어있음;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestFixture.createTableGroupRequest;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceTestFixture.단체_지정_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.acceptance.MenuAcceptanceTestFixture;
import kitchenpos.acceptance.MenuGroupAcceptanceTestFixture;
import kitchenpos.acceptance.ProductAcceptanceTestFixture;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 인수 테스트")
public class OrderTableAcceptanceTest extends OrderTableAcceptanceTestFixture {

    /**
     *   When 주문 테이블을 생성하면
     *   Then 주문 테이블이 생성된다
     */
    @DisplayName("주문_테이블을 생성한다")
    @Test
    void 주문_테이블_생성() {
        // When
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(주문_테이블_1);

        // Then
        주문_테이블_생성됨(response);

        // Then
        OrderTableResponse 생성된_주문_테이블 = 주문_테이블(response);
        assertAll(
                () -> assertThat(생성된_주문_테이블.getId()).isNotNull(),
                () -> assertThat(생성된_주문_테이블.getTableGroupId()).isEqualTo(주문_테이블_1.getTableGroupId()),
                () -> assertThat(생성된_주문_테이블.getNumberOfGuests()).isEqualTo(주문_테이블_1.getNumberOfGuests()),
                () -> assertThat(생성된_주문_테이블.isEmpty()).isEqualTo(주문_테이블_1.isEmpty())
        );
    }

    /**
     *   Given 주문 테이블을 등록하고
     *   When 주문 테이블 목록을 조회화면
     *   Then 주문 테이블 목록이 조회된다
     */
    @DisplayName("주문_테이블 목록을 조회한다")
    @Test
    void 주문_테이블_목록_조회() {
        // Given
        OrderTableResponse 생성된_주문_테이블_1 = 주문_테이블_생성_되어있음(주문_테이블_1);
        OrderTableResponse 생성된_주문_테이블_2 = 주문_테이블_생성_되어있음(주문_테이블_2);
        // When
        ExtractableResponse<Response> response = 주문_테이블_조회_요청();

        // Then
        주문_테이블_목록_조회됨(response);

        // Then
        List<OrderTableResponse> 조회된_주문_테이블_목록 = 주문_테이블_목록(response);
        assertThat(조회된_주문_테이블_목록).containsAll(Arrays.asList(생성된_주문_테이블_1, 생성된_주문_테이블_2));
    }

    /**
     *   Given 주문 테이블을 등록하고
     *   When 주문 테이블의 빈 테이블 여부를 비어있지 않음으로 수정하면
     *   Then 주문 테이블의 빈 테이블 여부가 비어있지 않음으로 수정된다
     */
    @DisplayName("주문_테이블을 비어있지 않은 상태로 수정한다")
    @Test
    void 빈_테이블_비어있지_않음으로_수정() {
        // Given
        OrderTableResponse 생성된_주문_테이블 = 주문_테이블_생성_되어있음(주문_테이블_1);

        // When
        주문_테이블_1.setEmpty(false);
        ExtractableResponse<Response> response = 빈_테이블_여부_수정_요청(생성된_주문_테이블.getId(), 주문_테이블_1);

        // Then
        빈_테이블_여부_수정됨(response);

        // Then
        OrderTableResponse 수정된_주문_테이블 = 주문_테이블(response);
        assertAll(
                () -> assertThat(수정된_주문_테이블.getId()).isEqualTo(생성된_주문_테이블.getId()),
                () -> assertThat(수정된_주문_테이블.isEmpty()).isEqualTo(false)
        );
    }

    /**
     *   Given 빈 테이블이 아닌 주문 테이블을 등록하고
     *   When 주문 테이블을 빈 테이블로 수정하면
     *   Then 주문 테이블이 빈 테이블로 수정된다
     */
    @DisplayName("주문_테이블을 빈 테이블로 수정한다")
    @Test
    void 주문_테이블_빈_테이블로_수정() {
        // Given
        OrderTableResponse 생성된_주문_테이블 = 주문_테이블_생성_되어있음(주문_테이블_3);

        // When
        주문_테이블_3.setEmpty(true);
        ExtractableResponse<Response> response = 빈_테이블_여부_수정_요청(생성된_주문_테이블.getId(), 주문_테이블_3);

        // Then
        빈_테이블_여부_수정됨(response);

        // Then
        OrderTableResponse 수정된_주문_테이블 = 주문_테이블(response);
        assertAll(
                () -> assertThat(수정된_주문_테이블.getId()).isEqualTo(생성된_주문_테이블.getId()),
                () -> assertThat(수정된_주문_테이블.isEmpty()).isEqualTo(true)
        );
    }

    /**
     *   Given 빈 테이블이 아닌 주문 테이블을 등록하고
     *   When 주문 테이블의 방문한 손님 수를 수정하면
     *   Then 주문 테이블의 방문한 손님 수가 수정된다
     */
    @DisplayName("주문 테이블의 방문한 손님 수를 수정한다")
    @Test
    void 주문_테이블_방문한_손님_수_수정() {
        // Given
        OrderTableResponse 생성된_주문_테이블 = 주문_테이블_생성_되어있음(주문_테이블_3);

        // When
        주문_테이블_3.updateNumberOfGuests(5);
        ExtractableResponse<Response> response = 방문한_손님_수_수정_요청(생성된_주문_테이블.getId(), 주문_테이블_3);

        // Then
        방문한_손님_수_수정됨(response);

        // Then
        OrderTableResponse 수정된_주문_테이블 = 주문_테이블(response);
        assertAll(
                () -> assertThat(수정된_주문_테이블.getId()).isEqualTo(생성된_주문_테이블.getId()),
                () -> assertThat(수정된_주문_테이블.getNumberOfGuests()).isEqualTo(5)
        );
    }

    /**
     *   예외케이스
     *   When 생성되지 않은 주문 테이블의 빈 테이블 여부를 수정하면
     *   Then 예외처리되며 주문 테이블의 빈 테이블 여부가 수정되지 않는다
     */
    @DisplayName("생성되지 않은 주문 테이블의 빈 테이블 여부 수정")
    @Test
    void 생성되지_않은_주문_테이블_빈_테이블_여부_수정() {
        // When
        주문_테이블_1.setEmpty(false);
        ExtractableResponse<Response> response = 빈_테이블_여부_수정_요청(1L, 주문_테이블_1);

        // Then
        빈_테이블_여부_수정되지_않음(response);
    }

    /**
     *   예외케이스
     *   Given 주문 테이블을 생성하고
     *   And   단체 지정한 후
     *   When  빈 테이블로 수정하면
     *   Then  예외처리되며 주문 테이블의 빈 테이블 여부가 수정되지 않는다
     */
    @DisplayName("단체 지정된 주문 테이블을 빈 테이블로 수정 요청")
    @Test
    void 단체_지정된_주문_테이블_빈_테이블로_수정() {
        // Given
        OrderTableResponse 생성된_주문_테이블_1 = 주문_테이블_생성_되어있음(주문_테이블_1);
        OrderTableResponse 생성된_주문_테이블_2 = 주문_테이블_생성_되어있음(주문_테이블_2);
        List<OrderTableResponse> 주문_테이블_목록 = Arrays.asList(생성된_주문_테이블_1, 생성된_주문_테이블_2);
        TableGroupRequest 단체_1 = createTableGroupRequest(주문_테이블_목록);
        단체_지정_생성_요청(단체_1);

        // When
        ExtractableResponse<Response> response = 빈_테이블_여부_수정_요청(생성된_주문_테이블_1.getId(), 주문_테이블_1);

        // Then
        빈_테이블_여부_수정되지_않음(response);
    }

    /**
     *   예외케이스
     *   Given 빈 테이블이 아닌 주문 테이블을 생성하고
     *   And   주문을 등록한 후
     *   And   주문의 상태를 계산 완료 상태로 변경하고
     *   When  빈 테이블로 수정하면
     *   Then  예외처리되며 주문 테이블의 빈 테이블 여부가 수정되지 않는다
     */
    @DisplayName("주문 등록된 주문 테이블을 빈 테이블로 수정 요청")
    @Test
    void 주문_등록된_주문_테이블_빈_테이블로_수정() {
        // Given
        OrderTableResponse 생성된_주문_테이블 = 주문_테이블_생성_되어있음(주문_테이블_3);

        MenuGroupResponse 세트 = MenuGroupAcceptanceTestFixture.메뉴_그룹_생성되어있음(new MenuGroupRequest("세트"));
        ProductResponse 떡볶이 = ProductAcceptanceTestFixture.상품_생성_되어있음(new ProductRequest("떡볶이", BigDecimal.valueOf(4500)));
        ProductResponse 튀김 = ProductAcceptanceTestFixture.상품_생성_되어있음(new ProductRequest("튀김", BigDecimal.valueOf(2500)));
        ProductResponse 순대 = ProductAcceptanceTestFixture.상품_생성_되어있음(new ProductRequest("순대", BigDecimal.valueOf(4000)));

        MenuProductRequest 떡튀순_상품_떡볶이 = new MenuProductRequest(떡볶이.getId(), 1);
        MenuProductRequest 떡튀순_상품_튀김 = new MenuProductRequest(튀김.getId(), 1);
        MenuProductRequest 떡튀순_상품_순대 = new MenuProductRequest(순대.getId(), 1);
        MenuProductRequest 떡튀순_곱배기_상품_떡볶이 = new MenuProductRequest(떡볶이.getId(), 2);

        List<MenuProductRequest> 떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        List<MenuProductRequest> 떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);

        MenuResponse 떡튀순 = MenuAcceptanceTestFixture.메뉴_생성_되어있음(new MenuRequest("떡튀순", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_상품_목록));
        MenuResponse 떡튀순_곱배기 = MenuAcceptanceTestFixture.메뉴_생성_되어있음(
                new MenuRequest("떡튀순_곱배기", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_곱배기_상품_목록));

        OrderLineItemRequest 주문_아이템_1 = new OrderLineItemRequest(떡튀순.getId(), 2);
        OrderLineItemRequest 주문_아이템_2 = new OrderLineItemRequest(떡튀순_곱배기.getId(), 1);
        List<OrderLineItemRequest> 주문_아이템_목록 = Arrays.asList(주문_아이템_1, 주문_아이템_2);
        OrderRequest 주문 = new OrderRequest(생성된_주문_테이블.getId(), OrderStatus.COOKING.name(), 주문_아이템_목록);
        주문_등록_되어있음(주문);

        // When
        주문_테이블_3.setEmpty(true);
        ExtractableResponse<Response> response = 빈_테이블_여부_수정_요청(생성된_주문_테이블.getId(), 주문_테이블_3);

        // Then
        빈_테이블_여부_수정되지_않음(response);
    }

    /**
     *   예외케이스
     *   Given 빈 테이블이 아닌 주문 테이블을 등록하고
     *   When 방문한 손님 수를 0보다 작은 수로 수정하면
     *   Then 예외처리되며 주문 테이블의 방문한 손님 수가 수정되지 않는다
     */
    @DisplayName("방문한 손님 수를 0보다 작은 수로 수정 요청")
    @Test
    void 잘_못된_방문한_손님_수() {
        // Given
        OrderTableResponse 생성된_주문_테이블 = 주문_테이블_생성_되어있음(주문_테이블_3);

        // When
        주문_테이블_3.updateNumberOfGuests(-1);
        ExtractableResponse<Response> response = 방문한_손님_수_수정_요청(생성된_주문_테이블.getId(), 주문_테이블_3);

        // Then
        방문한_손님_수_수정되지_않음(response);
    }

    /**
     *   예외케이스
     *   When 생성되지 않은 주문 테이블의 방문한 손님 수를 수정하면
     *   Then 예외처리되며 주문 테이블의 방문한 손님 수가 수정되지 않는다
     */
    @DisplayName("생성되지 않은 주문 테이블의 방문한 손님 수 수정 요청")
    @Test
    void 생성되지_않은_주문_테이블_방문한_손님_수_수정() {
        // When
        주문_테이블_1.updateNumberOfGuests(2);
        ExtractableResponse<Response> response = 방문한_손님_수_수정_요청(1L, 주문_테이블_1);

        // Then
        방문한_손님_수_수정되지_않음(response);
    }

    /**
     *   예외케이스
     *   Given 빈 테이블을 생성하고
     *   When 방문한 손님 수를 수정하면
     *   Then 예외처리되며 주문 테이블의 방문한 손님 수가 수정되지 않는다
     */
    @DisplayName("빈 테이블 방문한 손님 수 수정 요청")
    @Test
    void 빈_테이블_방문한_손님_수_수정() {
        // Given
        OrderTableResponse 생성된_빈_주문_테이블 = 주문_테이블_생성_되어있음(주문_테이블_1);

        // When
        주문_테이블_1.updateNumberOfGuests(2);
        ExtractableResponse<Response> response = 방문한_손님_수_수정_요청(생성된_빈_주문_테이블.getId(), 주문_테이블_1);

        // Then
        방문한_손님_수_수정되지_않음(response);
    }
}
