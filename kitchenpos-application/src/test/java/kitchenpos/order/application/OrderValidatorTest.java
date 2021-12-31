package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

	@InjectMocks
	private OrderValidator orderValidator;
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private MenuRepository menuRepository;

	private OrderTable 테이블;

	@BeforeEach
	void setup() {
		테이블 = OrderTable.of(1L, 2, false);
	}

	@DisplayName("주문 시, 주문 테이블이 존재해야 한다")
	@Test
	void createTest4() {
		// given
		given(orderTableRepository.findById(테이블.getId())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> orderValidator.validateCreate(1L))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.NOT_FOUND.getMessage());
	}

	@DisplayName("주문 시, 주문 테이블이 빈 테이블 상태가 아니어야 한다")
	@Test
	void createTest5() {
		// given
		테이블 = OrderTable.of(1L, 2, true);
		given(orderTableRepository.findById(테이블.getId())).willReturn(Optional.of(테이블));

		// when, then
		assertThatThrownBy(() -> orderValidator.validateCreate(1L))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("주문 시, 모든 주문 항목 메뉴가 존재해야 한다")
	@Test
	void createTest3() {
		// given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 2L);
		OrderRequest request = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

		given(orderTableRepository.findById(테이블.getId())).willReturn(Optional.of(테이블));
		given(menuRepository.existsById(any())).willReturn(false);

		// when, then
		assertThatThrownBy(() -> orderValidator.validateCreate(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.NOT_FOUND.getMessage());
	}

}
