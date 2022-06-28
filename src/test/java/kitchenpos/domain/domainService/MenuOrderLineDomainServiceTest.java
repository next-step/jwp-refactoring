package kitchenpos.domain.domainService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import kitchenpos.dto.dto.OrderLineItemDTO;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuOrderLineDomainServiceTest {

    private MenuOrderLineDomainService menuOrderLineDomainService;

    @Mock
    private MenuRepository menuRepository;

    private OrderRequest orderRequest;

    @BeforeEach
    public void init() {
        menuOrderLineDomainService = new MenuOrderLineDomainService(menuRepository);

        OrderLineItemDTO orderLineItemDTO_1 = new OrderLineItemDTO();
        orderLineItemDTO_1.setQuantity(1L);
        orderLineItemDTO_1.setMenuId(1L);

        OrderLineItemDTO orderLineItemDTO_2 = new OrderLineItemDTO();
        orderLineItemDTO_2.setQuantity(2L);
        orderLineItemDTO_2.setMenuId(2L);

        orderRequest = new OrderRequest();
        orderRequest.setOrderTableId(1L);
        orderRequest.setOrderLineItems(Arrays.asList(orderLineItemDTO_1, orderLineItemDTO_2));
    }

    @Test
    @DisplayName("주문 넣을 메뉴가 없으면 에러가 발생한다.")
    public void orderWithNoMenuThrowError() {
        //given
        orderRequest.setOrderLineItems(new LinkedList<>());

        //when & then
        assertThatThrownBy(
            () -> menuOrderLineDomainService.validateComponentForCreateOrder(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 넣을 상품이 저장되어있지 않으면 에러가 발생한다..")
    public void orderWithDoNotSavedProductThrowError() {
        //given
        when(menuRepository.countByIdIn(any())).thenReturn(1);

        //when & then
        assertThatThrownBy(
            () -> menuOrderLineDomainService.validateComponentForCreateOrder(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}