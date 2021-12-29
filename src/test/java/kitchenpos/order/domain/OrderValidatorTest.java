package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.common.exception.KitchenposNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @DisplayName("유효햔 메뉴")
    @Test
    void validateMenu() {
        // given
        ID로_주문_테이블_조회(new OrderTable(1L, 4));
        ID로_메뉴_조회(new Menu());
        메뉴_개수_반환(1);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));

        // when and then
        assertThatCode(() -> orderValidator.validate(request))
            .doesNotThrowAnyException();
    }

    @DisplayName("메뉴가 없을 때 에러")
    @Test
    void validateMenuErrorWhenMenuIsNull() {
        // given
        ID로_주문_테이블_조회(new OrderTable(1L, 4));
        ID로_메뉴_조회(null);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> orderValidator.validate(request));
    }

    @DisplayName("메뉴의 개수가 요청으로 들어온 개수와 다르면 에러")
    @Test
    void validateMenuErrorWhenInvalidMenuSize() {
        // given
        ID로_주문_테이블_조회(new OrderTable(1L, 4));
        ID로_메뉴_조회(new Menu());
        메뉴_개수_반환(2);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderValidator.validate(request))
            .withMessage("주문 항목의 개수가 다릅니다.");
    }

    @DisplayName("테이블이 존재하지 않으면 에러")
    @Test
    void validateTableErrorWhenNotExists() {
        // given
        ID로_주문_테이블_조회(null);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));

        // when and then
        assertThatExceptionOfType(KitchenposNotFoundException.class)
            .isThrownBy(() -> orderValidator.validate(request));
    }

    @DisplayName("테이블이 비어있으면 에러")
    @Test
    void validateTableErrorWhenTableEmpty() {
        // given
        ID로_주문_테이블_조회(new OrderTable(1L, 1L, 4, true));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> orderValidator.validate(request))
            .withMessage("주문 테이블이 비어있습니다.");
    }

    private void ID로_주문_테이블_조회(OrderTable orderTable) {
        Mockito.when(orderTableRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.ofNullable(orderTable));

    }

    private void ID로_메뉴_조회(Menu menu) {
        Mockito.when(menuRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.ofNullable(menu));
    }

    private void 메뉴_개수_반환(long count) {
        Mockito.when(menuRepository.countByIdIn(Mockito.anyList()))
            .thenReturn(count);
    }
}
