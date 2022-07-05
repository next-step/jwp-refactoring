package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.validator.TableGroupValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @Test
    @DisplayName("테이블 그룹 등록시 정상 테이블 정보 일때 테이블 그룹이 등록 된다")
    void create() {
        // given
        OrderTable 단체_주문_테이블_2명 = OrderTable.of(1L, null, NumberOfGuests.of(2), Empty.of(true));
        OrderTable 단체_주문_테이블_3명 = OrderTable.of(2L, null, NumberOfGuests.of(3), Empty.of(true));

        TableGroup 단체_주문_테이블 = TableGroup.of(2L, LocalDateTime.now());

        TableGroupRequest 단체_주문_테이블_요청 =
                TableGroupRequest.of(Arrays.asList(
                        OrderTableIdRequest.of(단체_주문_테이블_2명.getId()),
                        OrderTableIdRequest.of(단체_주문_테이블_3명.getId()))
                );

        // when
        when(tableGroupRepository.save(any())).thenReturn(단체_주문_테이블);

        TableGroupResponse 테이블_그룹_생성_결과 = tableGroupService.create(단체_주문_테이블_요청);

        // then
        Assertions.assertThat(테이블_그룹_생성_결과).isEqualTo(TableGroupResponse.of(단체_주문_테이블));
    }

    @Test
    @DisplayName("테이블 그룹 해체시 테이블 그룹이 해제 가능한 상태라면 해제 한다")
    void ungroup() {
        OrderTable 단체_주문_테이블_10명 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(true));
        OrderTable 단체_주문_테이블_20명 = OrderTable.of(2L, null, NumberOfGuests.of(4), Empty.of(true));

        TableGroup 요청_단체_테이블_그룹 = TableGroup.of(999L, LocalDateTime.now());

        // when
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(요청_단체_테이블_그룹));

        tableGroupService.ungroup(요청_단체_테이블_그룹.getId());

        // then
        assertAll(
                () -> Assertions.assertThat(단체_주문_테이블_10명.getTableGroupId()).isNull(),
                () -> Assertions.assertThat(단체_주문_테이블_20명.getTableGroupId()).isNull()
        );
    }

}
