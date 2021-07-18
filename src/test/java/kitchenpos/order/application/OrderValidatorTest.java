package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private OrderValidator orderValidator;

	private OrderRequest orderRequest;

	@BeforeEach
	void setUp() {
		orderRequest = new OrderRequest(1L, null, Arrays.asList(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L)));
	}

	@Test
	@DisplayName("주문 생성 시 order line item 의 개수와 메뉴의 숫자가 일치하지 않으면 익셉션 발생")
	void orderValidateTest() {
		when(menuRepository.countByIdIn(Lists.list(1L, 2L))).thenReturn(1L);

		assertThatThrownBy(() -> orderValidator.validate(orderRequest))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 생성 시 주문 테이블이 빈 테이블이면 익셉션 발생")
	void orderValidateTest2() {
		when(menuRepository.countByIdIn(Lists.list(1L, 2L))).thenReturn(2L);
		when(orderTableRepository.existsByIdAndEmptyFalse(1L)).thenReturn(false);

		assertThatThrownBy(() -> orderValidator.validate(orderRequest))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
