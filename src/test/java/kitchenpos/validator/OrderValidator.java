package kitchenpos.validator;

import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class OrderValidator {

    @Test
    @DisplayName("주문 항목이 비어 있을 수 없다.")
    void createOrderEmpty() {
/*        given(menuPort.findAllByMenuId(any())).willReturn(Arrays.asList(후치콜세트));


        assertThatThrownBy(() ->
                orderService.create(new OrderRequest(1L, null))
        ).isInstanceOf(IllegalArgumentException.class);*/
    }

}
