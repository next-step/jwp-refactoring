package kitchenpos.order.domain;

import static common.MenuFixture.메뉴_양념치킨;
import static common.OrderTableFixture.첫번째_주문테이블;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.Message;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidationTest {

    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private OrderValidation orderValidation;

    @Test
    void 주문상품의_숫자와_메뉴에_등록된_숫자가_다른경우_예외() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();
        Menu 메뉴_양념치킨 = 메뉴_양념치킨();

        // mocking
        when(menuDao.countByIdIn(any(List.class))).thenReturn(3L);

        assertThatThrownBy(() -> {
            OrderRequest orderRequest = new OrderRequest(첫번째_주문테이블.getId(),
                asList(new OrderLineRequest(메뉴_양념치킨.getId(), 1L)));

            orderValidation.valid(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_SIZE_IS_NOT_EQUALS.getMessage());
    }
}