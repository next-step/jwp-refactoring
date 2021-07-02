package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dto.TableGroupRequest;

@DisplayName("주문테이블그룹 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("등록된 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithUnknownOrderTableTest() {
		// given
		TableGroupRequest mockRequest = mock(TableGroupRequest.class);
		when(mockRequest.getOrderTableIds()).thenReturn(asList(1L, 2L));
		when(orderTableRepository.findAllById(anyList())).thenReturn(new ArrayList<>());

		// when
		assertThatThrownBy(() -> tableGroupService.create(mockRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 되지 않은 주문테이블은 그룹화 할 수 없습니다.");
	}

}
