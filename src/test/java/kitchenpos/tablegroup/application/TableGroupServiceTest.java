package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.fixture.TableFixture;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.fixture.TableGroupFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    private OrderTable 비어있는_테이블1;
    private OrderTable 비어있는_테이블2;
    private TableGroup 그룹_테이블;

    @BeforeEach
    public void setUp() {
        비어있는_테이블1 = TableFixture.create(1L, null, 0, true);
        비어있는_테이블2 = TableFixture.create(2L, null, 0, true);
        그룹_테이블 = TableGroupFixture.create(1L, LocalDateTime.now());
    }

    @DisplayName("테이블 그룹 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        TableGroupRequest 요청_테이블_그룹 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(비어있는_테이블1.getId()), OrderTableIdRequest.of(비어있는_테이블2.getId())));

        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(그룹_테이블);

        // when
        TableGroupResponse 생성된_테이블_그룹 = tableGroupService.create(요청_테이블_그룹);

        // then
        assertThat(생성된_테이블_그룹).isEqualTo(TableGroupResponse.of(그룹_테이블));
    }

    @DisplayName("테이블 그룹 해제 성공 테스트")
    @Test
    void ungroup_success() {
        // given
        given(tableGroupRepository.findById(any(Long.class))).willReturn(Optional.of(그룹_테이블));

        // when & then
        Assertions.assertThatNoException()
                .isThrownBy(() -> tableGroupService.ungroup(그룹_테이블.getId()));

    }
}
