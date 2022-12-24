package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.exception.GroupTableRequestException;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("[단체지정] 주문테이블 목록이 비어있거나 2개 미만인 경우 할 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Collections.emptyList())))
            .isInstanceOf(GroupTableRequestException.class);
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Collections.singletonList(1L))))
            .isInstanceOf(GroupTableRequestException.class);
    }

    @DisplayName("[단체지정] 등록되어있는 주문테이블만 가능하다")
    @Test
    void test2() {
        when(orderTableRepository.findAllById(any())).thenReturn(Collections.emptyList());

        assertThatThrownBy(
            () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L))))
            .isInstanceOf(GroupTableRequestException.class);
    }

}
