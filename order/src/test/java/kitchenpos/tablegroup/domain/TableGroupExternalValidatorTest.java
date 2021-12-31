package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.repo.OrderRepository;
import kitchenpos.ordertable.domain.domain.OrderTable;
import kitchenpos.tablegroup.domain.domain.TableGroup;
import kitchenpos.tablegroup.domain.service.TableGroupExternalValidator;
import kitchenpos.tablegroup.exception.CanNotUngroupByOrderStatusException;

@ExtendWith(MockitoExtension.class)
class TableGroupExternalValidatorTest {

	@InjectMocks
	private TableGroupExternalValidator tableGroupExternalValidator;

	@Mock
	private OrderRepository orderRepository;

	@DisplayName("그룹 해지: 주문 테이블의 주문 상태가 조리 혹은 식사 상태이면 예외발생")
	@Test
	void ungroup_order_table_status_cooking_or_meal() {
		final OrderTable 주문테이블1 = OrderTable.of(1L, null, 4, true);
		final OrderTable 주문테이블2 = OrderTable.of(2L, null, 3, true);
		TableGroup.of(1L, Arrays.asList(주문테이블1, 주문테이블2));

		given(orderRepository.existsByOrderTable_IdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

		assertThatExceptionOfType(CanNotUngroupByOrderStatusException.class)
			.isThrownBy(() -> tableGroupExternalValidator.ungroup(
				Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())
			));
	}
}
