package kitchenpos.tableGroup.mapper;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.table.TableGenerator.주문_테이블_목록_생성;
import static kitchenpos.table.TableGenerator.주문_테이블_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static kitchenpos.tableGroup.TableGroupGenerator.테이블_그룹_생성;
import static kitchenpos.tableGroup.TableGroupGenerator.테이블_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupMapperTest {

    @Mock
    private TableService tableService;

    @InjectMocks
    private TableGroupMapper tableGroupMapper;

    private OrderTable 주문_테이블 = spy(주문_테이블_생성(손님_수_생성(10)));

    @BeforeEach
    void setUp() {
        when(주문_테이블.getId()).thenReturn(0L);
    }

    @DisplayName("단체 지정의 주문 테이블 수가 1개 이하로 요청이 오면 예외가 발생해야 한다")
    @Test
    void mapTableGroupByContainUnderOneOrderTable() {
        // given
        when(tableService.findOrderTablesByIds(Collections.singletonList(0L)))
                .thenReturn(주문_테이블_목록_생성(Collections.singletonList(주문_테이블)));
        TableGroupCreateRequest 주문_테이블_1개 = 테이블_그룹_생성_요청(Collections.singletonList(주문_테이블.getId()));
        TableGroupCreateRequest 주문_테이블_0개 = 테이블_그룹_생성_요청(Collections.emptyList());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupMapper.mapFrom(주문_테이블_1개));
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupMapper.mapFrom(주문_테이블_0개));
    }

    @DisplayName("단체 지정에 비어있지 않은 주문 테이블이 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void mapTableGroupIncludeNotEmptyTable() {
        // given
        when(주문_테이블.isEmpty()).thenReturn(false);
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));
        주문_테이블.joinGroup(테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블))));
        TableGroupCreateRequest 주문_테이블_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(주문_테이블.getId(), 주문_테이블.getId()));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupMapper.mapFrom(주문_테이블_생성_요청));
    }

    @DisplayName("단체 지정에 이미 단체 지정에 속한 주문 테이블이 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void mapTableGroupByAlreadyBelongGroupTest() {
        // given
        when(주문_테이블.isEmpty()).thenReturn(true);
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블)));
        주문_테이블.joinGroup(테이블_그룹_생성(주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블))));
        TableGroupCreateRequest 주문_테이블_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(주문_테이블.getId(), 주문_테이블.getId()));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupMapper.mapFrom(주문_테이블_생성_요청));
    }


    @DisplayName("정상 상태의 단체 지정 요청이 오면 단체 지정 생성이 성공되어야 한다")
    @Test
    void mapTableGroupTest() {
        // given
        OrderTables 주문_테이블_목록 = 주문_테이블_목록_생성(Arrays.asList(주문_테이블, 주문_테이블));
        TableGroupCreateRequest 주문_테이블_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(주문_테이블.getId(), 주문_테이블.getId()));
        when(주문_테이블.isEmpty()).thenReturn(true);
        when(tableService.findOrderTablesByIds(any())).thenReturn(주문_테이블_목록);

        // when
        TableGroup 생성_결과 = tableGroupMapper.mapFrom(주문_테이블_생성_요청);

        // then
        assertThat(생성_결과).isNotNull();
    }
}
