package kitchenpos.acceptance;

import static kitchenpos.__fixture__.MenuProductTestFixture.메뉴_상품_1개_생성;
import static kitchenpos.__fixture__.OrderLineItemTestFixture.주문_항목_생성;
import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.OrderAcceptanceTest.주문_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceTest.테이블_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private Long 두마리_메뉴_아이디;
    private Long 후라이드_아이디;
    private Long 양념_아이디;
    private Menu 메뉴_1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        두마리_메뉴_아이디 = 메뉴_그룹_생성_요청("두마리메뉴").jsonPath().getLong("id");
        후라이드_아이디 = 상품_생성_요청("후라이드", 16_000).jsonPath().getLong("id");
        양념_아이디 = 상품_생성_요청("양념", 17_000).jsonPath().getLong("id");
        메뉴_1 = 메뉴_생성_요청(
                "후라이드양념",
                31_000,
                두마리_메뉴_아이디,
                메뉴_상품_1개_생성(후라이드_아이디),
                메뉴_상품_1개_생성(양념_아이디)
        ).as(Menu.class);
    }

    @DisplayName("테이블 생성에 성공한다.")
    @Test
    void createTable() {
        //given
        final int 손님_수 = 3;
        final boolean 빈자리_여부 = false;

        //when
        final ExtractableResponse<Response> 결과 = 테이블_생성_요청(손님_수, 빈자리_여부);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(결과.jsonPath().getInt("numberOfGuests")).isEqualTo(3);
    }

    @DisplayName("테이블을 조회할 수 있다.")
    @Test
    void findTables() {
        //given
        테이블_생성_요청(3, false);

        //when
        final ExtractableResponse<Response> 테이블_목록 = 테이블_목록_조회();

        //then
        assertThat(테이블_목록.jsonPath().getList(".").size()).isEqualTo(1);
        assertThat(테이블_목록.jsonPath().getInt("[0].numberOfGuests")).isEqualTo(3);
    }

    @DisplayName("테이블을 빈 테이블로 변경할 수 있다.")
    @Test
    void changeTableEmpty() {
        //given
        final OrderTable 생성된_테이블 = 테이블_생성_요청(3, false).as(OrderTable.class);
        생성된_테이블.setEmpty(true);

        //when
        final ExtractableResponse<Response> 결과 = 빈_테이블_업데이트(생성된_테이블.getId(), 생성된_테이블);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(결과.jsonPath().getBoolean("empty")).isEqualTo(true);
    }

    @DisplayName("빈 테이블 변경 시 테이블 그룹 아이디가 존재하면 실패한다.")
    @Test
    void changeTableNumberOfGuestsFailedWhenTableAlreadyGrouped() {
        //given
        final OrderTable 생성된_테이블_1 = 테이블_생성_요청(3, true).as(OrderTable.class);
        final OrderTable 생성된_테이블_2 = 테이블_생성_요청(5, true).as(OrderTable.class);
        테이블_그룹_생성_요청(생성된_테이블_1, 생성된_테이블_2).as(TableGroup.class);
        생성된_테이블_1.setEmpty(true);

        //when
        final ExtractableResponse<Response> 결과 = 빈_테이블_업데이트(생성된_테이블_1.getId(), 생성된_테이블_1);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("빈 테이블 변경 시 주문에 테이블이 존재하면서 조리중이거나 식사중 상태이면 실패한다.")
    @Test
    void changeTableNumberOfGuestsFailedWhenOrderExistsAndCookingOrMeal() {
        //given
        final OrderTable 생성된_테이블_1 = 테이블_생성_요청(3, false).as(OrderTable.class);
        final Long 메뉴_아이디 = 메뉴_1.getId();
        final Long 메뉴_수량 = 메뉴_1.getMenuProducts()
                .stream()
                .mapToLong(menuProduct -> menuProduct.getQuantity())
                .sum();
        final OrderLineItem 주문_항목 = 주문_항목_생성(메뉴_아이디, 메뉴_수량);
        주문_생성_요청(생성된_테이블_1.getId(), "COOKING", 주문_항목).as(Order.class);
        생성된_테이블_1.setEmpty(true);

        //when
        final ExtractableResponse<Response> 결과 = 빈_테이블_업데이트(생성된_테이블_1.getId(), 생성된_테이블_1);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블 손님 수를 변경할 수 있다.")
    @Test
    void changeTableNumberOfGuests() {
        //given
        final OrderTable 생성된_테이블 = 테이블_생성_요청(3, false).as(OrderTable.class);
        생성된_테이블.setNumberOfGuests(5);

        //when
        final ExtractableResponse<Response> 결과 = 테이블_손님수_업데이트(생성된_테이블.getId(), 생성된_테이블);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(결과.jsonPath().getInt("numberOfGuests")).isEqualTo(5);
    }

    @DisplayName("테이블 손님 수를 변경할 때 변경할 손님 수가 음수이면 실패한다.")
    @Test
    void changeTableNumberOfGuestsFailedWhenNumberOfGuestsIsMinus() {
        //given
        final OrderTable 생성된_테이블 = 테이블_생성_요청(3, false).as(OrderTable.class);
        생성된_테이블.setNumberOfGuests(-2);

        //when
        final ExtractableResponse<Response> 결과 = 테이블_손님수_업데이트(생성된_테이블.getId(), 생성된_테이블);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테이블 손님 수를 변경할 때 비어있는 테이블이면 실패한다.")
    @Test
    void changeTableNumberOfGuestsFailedWhenTableIsEmpty() {
        //given
        final OrderTable 생성된_테이블 = 테이블_생성_요청(3, true).as(OrderTable.class);
        생성된_테이블.setNumberOfGuests(5);

        //when
        final ExtractableResponse<Response> 결과 = 테이블_손님수_업데이트(생성된_테이블.getId(), 생성된_테이블);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 빈_테이블_업데이트(final Long orderTableId, final OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("orderTableId", orderTableId)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/empty")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_손님수_업데이트(final Long orderTableId, final OrderTable orderTable) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("orderTableId", orderTableId)
                .body(orderTable)
                .when().put("/api/tables/{orderTableId}/number-of-guests")
                .then().log().all()
                .extract();
    }
}
