package kitchenpos.table.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("테이블 그룹 서비스 테스트")
@ExtendWith({MockitoExtension.class})
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private final OrderTable orderTable_1 = OrderTable.of(0, true);
    private final OrderTable orderTable_2 = OrderTable.of(0, true);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderTable_1, "id", 1L);
        ReflectionTestUtils.setField(orderTable_2, "id", 2L);
    }

    @Test
    @DisplayName("단체를 지정할 수 있다.")
    void create() {

        assertAll(
            () -> assertFalse(orderTable_1.isNotEmptyTableGroup()),
            () -> assertFalse(orderTable_2.isNotEmptyTableGroup())
        );

        when(orderTableRepository.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(tableGroupRepository.save(any(TableGroup.class)))
            .thenReturn(TableGroup.create());

        TableGroupResponse saved = tableGroupService.create(Arrays.asList(1L, 2L));

        assertAll(
            () -> assertNotNull(saved.getCreatedDate()),
            () -> assertTrue(orderTable_1.isNotEmptyTableGroup()),
            () -> assertTrue(orderTable_2.isNotEmptyTableGroup())
        );
    }

    @Test
    @DisplayName("단체 지정을 해제한다.(삭제한다)")
    void upgroup() {
        TableGroup tableGroup = TableGroup.create();
        tableGroup.addOrderTables(Arrays.asList(orderTable_1, orderTable_2));

        assertTrue(orderTable_1.isNotEmptyTableGroup());
        assertTrue(orderTable_2.isNotEmptyTableGroup());

        when(tableGroupRepository.findById(anyLong()))
            .thenReturn(Optional.of(tableGroup));

        tableGroupService.ungroup(1L);

        assertFalse(orderTable_1.isNotEmptyTableGroup());
        assertFalse(orderTable_2.isNotEmptyTableGroup());
        assertTrue(tableGroup.isEmpty());
    }

}