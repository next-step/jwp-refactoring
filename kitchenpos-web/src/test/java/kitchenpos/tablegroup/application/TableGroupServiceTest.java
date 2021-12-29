package kitchenpos.tablegroup.application;

import javafx.scene.control.Tab;
import kitchenpos.table.application.TableValidator;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.exception.NotSupportUngroupException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.fixtures.MenuFixtures.*;
import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.menu.fixtures.MenuProductFixtures.*;
import static kitchenpos.table.fixtures.OrderTableFixtures.*;
import static kitchenpos.product.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.tablegroup.fixtures.TableGroupFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * packageName : kitchenpos.application
 * fileName : TableGroupServiceTest
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
@DisplayName("그룹 테이블 통합 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블을 그룹화할 수 있다.")
    public void group() {
        //given
        TableGroup tableGroup = new TableGroup();
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

        // when
        TableGroupResponse actual = tableGroupService.create(그룹테이블_그룹요청());

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹화를 해제할 수 있다.")
    public void ungroup() {
        // given
        final TableGroup tableGroup = new TableGroup();
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(tableGroup));

        // when
        tableGroupService.ungroup(1L);

        // then
        verify(tableGroupRepository).delete(tableGroup);
    }

    @Test
    @DisplayName("존재하지 않은 테이블 그룹은 해제할 수 없다.")
    public void ungroupFailByUnknownGroupTable() {
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(TableGroupNotFoundException.class);
    }


}
