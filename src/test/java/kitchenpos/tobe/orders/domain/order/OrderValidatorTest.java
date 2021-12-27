package kitchenpos.tobe.orders.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.tobe.fixture.MenuFixture;
import kitchenpos.tobe.fixture.MenuProductFixture;
import kitchenpos.tobe.fixture.MenuProductsFixture;
import kitchenpos.tobe.fixture.OrderFixture;
import kitchenpos.tobe.fixture.OrderLineItemFixture;
import kitchenpos.tobe.fixture.OrderTableFixture;
import kitchenpos.tobe.menus.domain.FakeMenuValidator;
import kitchenpos.tobe.menus.domain.MenuRepository;
import kitchenpos.tobe.orders.domain.ordertable.OrderTable;
import kitchenpos.tobe.orders.domain.ordertable.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderValidator validator;

    @DisplayName("주문 메뉴를 검증할 수 있다.")
    @Nested
    class ValidateOrderMenuTest {

        @DisplayName("1개 이상의 메뉴를 포함하지 않으면 주문을 요청할 수 없다.")
        @Test
        void atLeastOneMenu() {
            // given
            final Long orderId = 1L;
            final Long orderTableId = 1L;
            final OrderLineItems items = OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(1L, null, 1L)
            );

            // when
            final ThrowableAssert.ThrowingCallable request = () -> OrderFixture.of(
                orderId,
                orderTableId,
                items,
                validator
            );

            // then
            assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("등록 되어 있지 않은 메뉴로 주문을 요청할 수 없다.")
        @Test
        void menuExist() {
            // given
            final Long orderId = 1L;
            final Long orderTableId = 1L;
            final OrderLineItems items = OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(1L, 1L, 1L)
            );

            // when
            final ThrowableAssert.ThrowingCallable request = () -> OrderFixture.of(
                orderId,
                orderTableId,
                items,
                validator
            );

            // then
            assertThatThrownBy(request).isInstanceOf(NoSuchElementException.class);
        }
    }

    @DisplayName("주문 테이블을 검증할 수 있다.")
    @Nested
    class ValidateOrderTableTest {

        @DisplayName("등록되어 있지 주문 테이블에 주문을 요청할 수 없다.")
        @Test
        void orderTableExists() {
            // given
            final Long orderId = 1L;
            final Long orderTableId = 1L;
            final OrderLineItems items = OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(1L, 1L, 1L)
            );

            given(menuRepository.findAllByIdIn(anyList())).willReturn(
                Collections.singletonList(
                    MenuFixture.of(1L,
                        "메뉴",
                        1_000L,
                        MenuProductsFixture.of(MenuProductFixture.of(1L, 1_000L, 1L)),
                        1L,
                        new FakeMenuValidator()
                    )
                )
            );

            // when
            final ThrowableAssert.ThrowingCallable request = () -> OrderFixture.of(
                orderId,
                orderTableId,
                items,
                validator
            );

            // then
            assertThatThrownBy(request).isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("빈 주문 테이블에 주문을 등록할 수 없다.")
        @Test
        void orderTableEmpty() {
            // given
            final Long orderId = 1L;
            final OrderTable orderTable = OrderTableFixture.of(1L);
            final OrderLineItems items = OrderLineItemFixture.ofList(
                OrderLineItemFixture.of(1L, 1L, 1L)
            );

            given(menuRepository.findAllByIdIn(anyList())).willReturn(
                Collections.singletonList(
                    MenuFixture.of(
                        1L,
                        "메뉴",
                        1_000L,
                        MenuProductsFixture.of(MenuProductFixture.of(1L, 1_000L, 1L)),
                        1L,
                        new FakeMenuValidator()
                    )
                )
            );

            given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));

            // when
            final ThrowableAssert.ThrowingCallable request = () -> OrderFixture.of(
                orderId,
                orderTable.getId(),
                items,
                validator
            );

            // then
            assertThatThrownBy(request).isInstanceOf(IllegalStateException.class);
        }
    }
}
