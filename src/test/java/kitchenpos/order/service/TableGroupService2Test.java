package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.order.domain.entity.OrderTable;
import kitchenpos.order.domain.entity.TableGroup;
import kitchenpos.order.domain.value.NumberOfGuests;
import kitchenpos.order.domain.value.OrderTables;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupService2Test {

    @Autowired
    TableGroupService tableGroupService;

    OrderTable 오더테이블_테이블1;
    OrderTableRequest 오더테이블_테이블1_리퀘스트;
    OrderTable 오더테이블_테이블2;
    OrderTableRequest 오더테이블_테이블2_리퀘스트;

    TableGroup 테이블그룹_테이블1_테이블2;
    TableGroupRequest 테이블그룹_테이블1_테이블2_리퀘스트;
    TableGroupRequest 테이블그룹_테이블1_리퀘스트;

    @BeforeEach
    void setUp() {
        오더테이블_테이블1 = new OrderTable(1L, null, NumberOfGuests.of(44), true);
        오더테이블_테이블1_리퀘스트 = new OrderTableRequest(11L, null, 3, true);
        오더테이블_테이블2 = new OrderTable(2L, null, NumberOfGuests.of(33), true);
        오더테이블_테이블2_리퀘스트 = new OrderTableRequest(12L, null, 3, true);

        테이블그룹_테이블1_테이블2 = new TableGroup(1L,
            new OrderTables(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2)));
        테이블그룹_테이블1_테이블2_리퀘스트 = new TableGroupRequest(
            Arrays.asList(오더테이블_테이블1_리퀘스트, 오더테이블_테이블2_리퀘스트));

        테이블그룹_테이블1_리퀘스트 = new TableGroupRequest(Arrays.asList(오더테이블_테이블1_리퀘스트));
    }

    @Test
    @DisplayName("단체 테이블을 생성한다.")
    void create() {
        //when
        TableGroupResponse createdTableGroup = tableGroupService.create(테이블그룹_테이블1_테이블2_리퀘스트);

        //then
        assertThat(createdTableGroup).isNotNull();
    }

    @Test
    @DisplayName("1개의 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_counting_is_one() {
        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("동일 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_same_orderTables() {
        //given
        TableGroupRequest 테이블그룹_테이블1_테이블1_리퀘스트 = new TableGroupRequest(
            Arrays.asList(오더테이블_테이블1_리퀘스트, 오더테이블_테이블1_리퀘스트));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블1_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈테이블이 아닐경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_orderTable_is_not_empty() {
        //given
        오더테이블_테이블2_리퀘스트 = new OrderTableRequest(2L, null, 3, false);
        오더테이블_테이블2 = new OrderTable(2L, NumberOfGuests.of(4), false);

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체테이블에 포함되어있는 테이블일경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_has_table_group() {
        //given
        TableGroup tableGroup = new TableGroup();
        오더테이블_테이블2 = new OrderTable(2L, tableGroup, NumberOfGuests.of(4), true);

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup() {
        //when
        tableGroupService.ungroup(99L);

        //then
        assertAll(() -> {
            assertThat(오더테이블_테이블1.getTableGroup()).isNull();
            assertThat(오더테이블_테이블2.getTableGroup()).isNull();
        });
    }

    @Test
    @DisplayName("조리중이거나 식사중일 경우 해체에 실패한다.")
    void ungroup_with_exception_when_orderStatus_is_cooking_or_meal() {
        //when && then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹_테이블1_테이블2.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}