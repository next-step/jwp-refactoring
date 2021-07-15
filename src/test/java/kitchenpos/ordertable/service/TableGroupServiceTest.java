package kitchenpos.ordertable.service;

import static kitchenpos.ordertable.domain.OrderTableTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.generic.exception.OrderTableNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 그룹 서비스")
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    TableService tableService;

    List<OrderTableRequest> 테이블_리스트_요청;
    TableGroupRequest 테이블_그룹_요청;

    @BeforeEach
    void setUp() {
        테이블_리스트_요청 = Arrays.asList(
            new OrderTableRequest(3L, 0, true),
            new OrderTableRequest(4L, 0, true));
        테이블_그룹_요청 = new TableGroupRequest(테이블_리스트_요청);
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패(주문 테이블 DB 검증 실패)")
    void create_failed2() {
        // given
        when(tableService.findById(테이블3.getId())).thenReturn(테이블3);
        when(tableService.findById(테이블4.getId())).thenThrow(OrderTableNotFoundException.class);

        // then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_요청))
            .isInstanceOf(OrderTableNotFoundException.class);
    }
}
