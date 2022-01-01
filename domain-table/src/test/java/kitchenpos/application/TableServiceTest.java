package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    TableService tableService;

    OrderTableRequest 빈_테이블_요청;
    OrderTableRequest 주문_테이블_요청;
    Menu 후라이드_후라이드;
    OrderTable 주문_테이블;
    OrderLineItems 주문_항목들;

    @BeforeEach
    void setUp() {
        빈_테이블_요청 = OrderTableRequest.of(0, true);
        주문_테이블_요청 = OrderTableRequest.of(5, false);

        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");
        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨.getId(), 2);
        후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));
        주문_테이블 = OrderTableFixture.of(4, false);
        주문_항목들 = OrderLineItems.from(Collections.singletonList(OrderLineItem.of(후라이드_후라이드, 1L)));
    }

    @Test
    void 빈_테이블_생성() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        BDDMockito.given(orderTableRepository.save(ArgumentMatchers.any())).willReturn(빈_테이블);

        // when
        OrderTableResponse actual = tableService.create(빈_테이블_요청);

        // then
        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isZero();
            assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @Test
    void 주문_테이블_조회() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        OrderTable 주문_테이블 = OrderTable.of(주문_테이블_요청.getNumberOfGuests(), 주문_테이블_요청.isEmpty());

        List<OrderTable> orderTables = Arrays.asList(빈_테이블, 주문_테이블);
        BDDMockito.given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).extracting("numberOfGuests")
                    .containsExactly(0, 5);
        });
    }

    @DisplayName("빈 테이블을 주문 테이블로 변경한다.")
    @Test
    void 주문_테이블_상태_변경() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        OrderTableRequest 주문_테이블_변경_요청 = OrderTableRequest.of(빈_테이블.getNumberOfGuests(), false);
        OrderTable 변경된_빈_테이블 = OrderTable.of(빈_테이블.getNumberOfGuests(), false);

        BDDMockito.given(orderTableRepository.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // when
        OrderTableResponse actual = tableService.changeEmpty(빈_테이블.getId(), 주문_테이블_변경_요청);

        // then
        assertThat(actual.isEmpty()).isEqualTo(변경된_빈_테이블.isEmpty());
    }

    @Test
    void 주문_테이블_상태_변경_시_테이블은_반드시_존재해야한다() {
        // given
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.empty());

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeEmpty(ArgumentMatchers.any(), 빈_테이블_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }


    @Test
    void 단체_지정_된_주문_테이블은_상태를_변경할_수_없다() {
        // given
        OrderTableRequest 단체_지정_테이블_요청 = OrderTableRequest.of(5, true);
        OrderTable 첫번째_단체_지정_테이블 = OrderTable.of(단체_지정_테이블_요청.getNumberOfGuests(), 단체_지정_테이블_요청.isEmpty());
        첫번째_단체_지정_테이블.group(1L);
        BDDMockito.given(orderTableRepository.findById(1L)).willReturn(Optional.of(첫번째_단체_지정_테이블));

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeEmpty(1L, 단체_지정_테이블_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("단체 지정 된 테이블은 상태를 변경할 수 없습니다.");
    }

    @Test
    void 방문한_손님_수_변경() {
        // given
        OrderTable 주문_테이블 = OrderTable.of(주문_테이블_요청.getNumberOfGuests(), 주문_테이블_요청.isEmpty());
        OrderTableRequest 방문한_손님_수_변경_요청 = OrderTableRequest.of(10, 주문_테이블_요청.isEmpty());
        OrderTable 방문자_수_변경된_주문_테이블 = OrderTable.of(방문한_손님_수_변경_요청.getNumberOfGuests(), 방문한_손님_수_변경_요청.isEmpty());

        BDDMockito.given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(주문_테이블.getId(), 방문한_손님_수_변경_요청);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(방문자_수_변경된_주문_테이블.getNumberOfGuests());
    }

    @Test
    void 방문한_손님_수_변경_시_빈_테이블이면_변경할_수_없다() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        BDDMockito.given(orderTableRepository.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(빈_테이블.getId(), 빈_테이블_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문상태가_조리이거나_식사이면_변경할_수_없다() {
        // given
        Order 주문 = Order.of(1L, 주문_항목들);
        주문.changeOrderStatus(OrderStatus.MEAL);
        OrderTable 주문_테이블 = OrderTable.ofOrder(주문_테이블_요청.getNumberOfGuests(), 주문_테이블_요청.isEmpty(), Arrays.asList(주문));
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(주문_테이블));

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> tableService.changeEmpty(빈_테이블_요청.getId(), 빈_테이블_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("주문 테이블의 상태가 조리이거나, 식사이면 변경할 수 없습니다.");
    }
}
