package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTable.Builder;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    private OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(orderTableRepository, menuRepository);
    }

    @Test
    @DisplayName("비어있지 않고 존재하는 주문 테이블이면 주문 테이블 번호를 반환한다.")
    void verifyValidOrderTable() {
        // given
        final OrderTable orderTable = new Builder()
                .setId(1L)
                .setEmpty(false)
                .build();
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        // when
        final Long orderTableId = orderValidator.notEmptyOrderTableId(1L);
        // then
        assertThat(orderTableId).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 에러 발생")
    void notExistOrderTable() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.notEmptyOrderTableId(1L));
    }

    @Test
    @DisplayName("주문 테이블이 존재하지만 비어있으면 에러 발생")
    void emptyOrderTable() {
        // given
        final OrderTable orderTable = new Builder()
                .setId(1L)
                .setEmpty(true)
                .build();
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        // when & then
        assertThatIllegalStateException()
                .isThrownBy(() -> orderValidator.notEmptyOrderTableId(1L));
    }

    @Test
    @DisplayName("주문 메뉴와 존재하는 메뉴가 갯수가 일치하는지 검증")
    void validOrderMenuCount() {
        // given
        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        // when && then
        orderValidator.validateOrderMenuCount(Arrays.asList(1L));
    }

    @Test
    @DisplayName("주문 메뉴와 존재하는 메뉴의 갯수가 일치하지 않으면 에러 발생")
    void invalidOrderMenuCount() {
        // given
        when(menuRepository.countByIdIn(any())).thenReturn(2L);
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateOrderMenuCount(Arrays.asList(1L)));
    }

    @Test
    @DisplayName("메뉴가 존재하면 메뉴 ID를 반환")
    void existMenu() {
        // given
        final Menu menu = new Menu.Builder("menu")
                .setId(1L)
                .build();
        when(menuRepository.findById(any())).thenReturn(Optional.of(menu));
        // when
        final Long menuId = orderValidator.existMenuId(1L);
        // then
        assertThat(menuId).isEqualTo(1L);
    }

    @Test
    @DisplayName("메뉴가 존재하지 않으면 예외 발생")
    void notExistMenu() {
        // given
        when(menuRepository.findById(any())).thenReturn(Optional.empty());
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.existMenuId(any()));
    }
}
