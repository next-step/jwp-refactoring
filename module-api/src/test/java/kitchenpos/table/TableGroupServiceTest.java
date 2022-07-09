package kitchenpos.table;

import kitchenpos.application.table.TableGroupService;
import kitchenpos.application.table.TableValidator;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.OrderTableIdsRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.repository.table.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.factory.TableGroupFixture.테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    TableGroupRepository tableGroupRepository;

    @Mock
    TableValidator tableValidator;

    TableGroup 단체;

    OrderTable 주문테이블1;
    OrderTable 주문테이블2;

    @Test
    @DisplayName("단체를 생성 및 지정한다.(Happy Path)")
    void create() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));
        given(tableValidator.findTableAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        willDoNothing().given(tableValidator).orderTablesSizeValidation(anyList(), any(TableGroupRequest.class));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체);

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(단체Request);

        //then
        assertThat(savedTableGroup).isNotNull()
                .satisfies(tableGroup -> {
                            tableGroup.getId().equals(단체.getId());
                            tableGroup.getOrderTableResponses()
                                        .stream()
                                        .map(OrderTableResponse::getId)
                                        .collect(Collectors.toList())
                                        .containsAll(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
                        }
                );
    }

    @Test
    @DisplayName("주문테이블 그룹에 포함된 주물테이블들 그룹 해제(Happy Path)")
    void ungroup() {
        //given
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        주문테이블1 = 테이블_생성(1L, 단체.getId(), 2, false);
        주문테이블2 = 테이블_생성(2L, 단체.getId(), 3, false);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(단체));
        willDoNothing().given(tableValidator).orderStatusByIdsValidate(anyList());

        //when
        tableGroupService.ungroup(단체.getId());

        //then
        assertThat(주문테이블1.getTableGroupId()).isNull();
        assertThat(주문테이블2.getTableGroupId()).isNull();
    }
}