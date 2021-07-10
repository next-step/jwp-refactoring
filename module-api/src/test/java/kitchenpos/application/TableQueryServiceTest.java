package kitchenpos.application;

import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.response.OrderTableViewResponse;
import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.미사용중인_테이블;
import static kitchenpos.fixture.OrderTableFixture.사용중인_2명_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TableQueryServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    private TableQueryService tableQueryService;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();

        tableQueryService = new TableQueryService(orderTableRepository);
    }

    @Test
    @DisplayName("list - 정상적인 주문 테이블 전체 조회")
    void 정상적인_주문_테이블_전체_조회() {
        // when
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(미사용중인_테이블, 사용중인_2명_테이블));

        List<OrderTableViewResponse> list = tableQueryService.list();

        // then
        assertThat(list).containsExactly(OrderTableViewResponse.of(미사용중인_테이블), OrderTableViewResponse.of(사용중인_2명_테이블));

        verify(orderTableRepository, VerificationModeFactory.times(1))
                .findAll();
    }
}