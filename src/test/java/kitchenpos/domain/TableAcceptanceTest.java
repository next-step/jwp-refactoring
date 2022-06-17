package kitchenpos.domain;

import static kitchenpos.domain.TableAcceptanceTestMethod.빈_테이블_변경_요청;
import static kitchenpos.domain.TableAcceptanceTestMethod.빈_테이블_변경됨;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_등록_요청;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_등록되어_있음;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_등록됨;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_목록_응답됨;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_목록_조회_요청;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_목록_포함됨;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_손님_수_변경_요청;
import static kitchenpos.domain.TableAcceptanceTestMethod.테이블_손님_수_변경됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 관련 인수테스트")
class TableAcceptanceTest extends AcceptanceTest {

    private OrderTable 빈_테이블;
    private OrderTable 주문_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        빈_테이블 = OrderTableFixtureFactory.create(true);
        주문_테이블 = OrderTableFixtureFactory.create(false);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 테이블_등록_요청(빈_테이블);

        // then
        테이블_등록됨(response);
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse1 = 테이블_등록되어_있음(빈_테이블);
        ExtractableResponse<Response> createdResponse2 = 테이블_등록되어_있음(주문_테이블);

        // when
        ExtractableResponse<Response> response = 테이블_목록_조회_요청();

        // then
        테이블_목록_응답됨(response);
        테이블_목록_포함됨(response, Lists.newArrayList(createdResponse1, createdResponse2));
    }

    @DisplayName("테이블의 상태를 빈 테이블 상태로 변경할 수 있다.")
    @Test
    void change01() {
        // given
        OrderTable createdOrderTable = 테이블_등록되어_있음(주문_테이블).as(OrderTable.class);
        createdOrderTable.setEmpty(true);

        // when
        ExtractableResponse<Response> response = 빈_테이블_변경_요청(createdOrderTable.getId(), createdOrderTable);

        // then
        빈_테이블_변경됨(response);
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change02() {
        // given
        OrderTable createdOrderTable = 테이블_등록되어_있음(주문_테이블).as(OrderTable.class);
        createdOrderTable.setNumberOfGuests(50);

        // when
        ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(createdOrderTable.getId(), createdOrderTable);

        // then
        테이블_손님_수_변경됨(response);
    }
}