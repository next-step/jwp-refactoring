package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.fixture.TestOrderTableFactory;
import kitchenpos.fixture.TestTableGroupFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 그룹 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableService tableService;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableGroupValidator tableGroupValidator;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블 단체 지정을 등록한다.")
    @Test
    void create() {
        final TableGroup 테이블그룹 = TestTableGroupFactory.테이블_그룹_생성됨(1L);
        final OrderTable 주문테이블1 = TestOrderTableFactory.주문_테이블_조회됨(1L, 5, false);
        final OrderTable 주문테이블2 = TestOrderTableFactory.주문_테이블_조회됨(2L, 5, false);
        final List<OrderTable> 주문테이블_목록 = Lists.newArrayList(주문테이블1, 주문테이블2);
        final List<Long> 주문테이블_ID_목록 = TestOrderTableFactory.주문_테이블_ID_목록(주문테이블_목록);
        final TableGroupRequest 테이블그룹_요청 = TestTableGroupFactory.테이블_그룹_요청(주문테이블_ID_목록);

        given(tableGroupRepository.save(any())).willReturn(테이블그룹);
        given(tableService.findAllById(anyList())).willReturn(주문테이블_목록);

        final TableGroupResponse actual = tableGroupService.group(테이블그룹_요청);

        TestTableGroupFactory.테이블_그룹_생성_확인함(actual, 테이블그룹);
    }

    @DisplayName("주문 테이블 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        final OrderTable 주문테이블1 = TestOrderTableFactory.주문_테이블_테이블그룹_설정(1L, null, 5, false);
        final OrderTable 주문테이블2 = TestOrderTableFactory.주문_테이블_테이블그룹_설정(2L, null, 5, false);
        final TableGroup 테이블그룹 = TestTableGroupFactory.테이블_그룹_추가됨(1L);

        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(테이블그룹));

        tableGroupService.ungroup(테이블그룹.getId());

        TestTableGroupFactory.테이블그룹_해제_확인됨(주문테이블1, 주문테이블2);
    }
}
