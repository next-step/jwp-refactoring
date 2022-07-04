package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 생성")
    @Nested
    class 단체_지정_생성 {
        @DisplayName("단체 지정을 생성할 수 있다.")
        @Test
        void 단체_지정_성공() {
            // given
            OrderTable 빈_주문_테이블 = new OrderTable(1L, 5, true);
            OrderTable 빈_주문_테이블2 = new OrderTable(2L, 3, true);
            TableGroup 단체_지정 = new TableGroup(Arrays.asList(빈_주문_테이블, 빈_주문_테이블2));

            given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈_주문_테이블, 빈_주문_테이블2));
            given(tableGroupDao.save(any(TableGroup.class))).willReturn(
                    new TableGroup(1L, LocalDateTime.now(), Arrays.asList(빈_주문_테이블, 빈_주문_테이블2)));

            // when
            TableGroup savedTableGroup = tableGroupService.create(단체_지정);

            // then
            assertThat(savedTableGroup.getId()).isNotNull();
        }

        @DisplayName("단체 지정 실패")
        @Nested
        class 단체_지정_실패 {
            @DisplayName("한 개이하의 주문 테이블을 포함한 주문의 경우 단체 지정할 수 없다.")
            @Test
            void 한_개이하의_주문_테이블을_포함한_단체_지정_생성() {
                // given
                TableGroup 단체_지정 = new TableGroup(Collections.singletonList(new OrderTable()));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 주문 테이블이 포함된 주문의 경우 단체 지정할 수 없다.")
            @Test
            void 존재하지_않는_주문_테이블이_포함된_단체_지정() {
                // given
                OrderTable 생성된_주문_테이블 = new OrderTable();
                OrderTable 존재하지_않는_주문_테이블 = new OrderTable();
                TableGroup 단체_지정 = new TableGroup(Arrays.asList(생성된_주문_테이블, 존재하지_않는_주문_테이블));

                given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.singletonList(생성된_주문_테이블));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("중복된 주문 테이블을 포함한 주문은 단체 지정할 수 없다.")
            @Test
            void 중복된_주문_테이블이_포함된_주문_단체_지정() {
                // given
                OrderTable 주문_테이블 = new OrderTable(1L, 5);
                OrderTable 중복된_주문_테이블 = new OrderTable(1L, 5);
                TableGroup 단체_지정 = new TableGroup(Arrays.asList(주문_테이블, 중복된_주문_테이블));

                given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.singletonList(주문_테이블));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("빈 주문 테이블이 아닌 주문 테이블을 포함한 주문을 단체 지정할 수 없다.")
            @Test
            void 빈_주문_테이블이_아닌_주문_테이블을_포함한_주문_단체_지정() {
                // given
                OrderTable 빈_주문_테이블 = new OrderTable(1L, 5, true);
                OrderTable 주문_테이블 = new OrderTable(2L, 5, false);
                TableGroup 단체_지정 = new TableGroup(Arrays.asList(빈_주문_테이블, 주문_테이블));

                given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈_주문_테이블, 주문_테이블));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정)).isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("이미 단체 지정된 주문 테이블이 포함된 주문의 경우 단체 지정할 수 없다.")
            @Test
            void 이미_단체_지정된_주문_테이블을_포함한_주문은_단체_지정_불가() {
                OrderTable 빈_주문_테이블 = new OrderTable(1L, 5, true);
                OrderTable 단체_지정_주문_테이블 = new OrderTable(2L, 1L, 5, true);
                TableGroup 단체_지정 = new TableGroup(Arrays.asList(빈_주문_테이블, 단체_지정_주문_테이블));

                given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈_주문_테이블, 단체_지정_주문_테이블));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("단체 지정 삭제")
    @Nested
    class 단체_지정_삭제 {
        @DisplayName("단체 지정을 삭제할 수 있다.")
        @Test
        void 단체_지정_삭제_성공() {
            OrderTable 빈_주문_테이블 = new OrderTable(1L, 1L, 5, Boolean.TRUE);

            given(orderTableDao.findAllByTableGroupId(any())).willReturn(Collections.singletonList(빈_주문_테이블));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),
                    eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))).willReturn(Boolean.FALSE);

            tableGroupService.ungroup(1L);

            assertThat(빈_주문_테이블.getTableGroupId()).isNull();
        }

        @DisplayName("주문상태가 계산 완료가 아니면 삭제할 수 없다.")
        @Test
        void 주문상태가_계산_완료가_아닌_경우_삭제_불가() {
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),
                    eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))).willReturn(Boolean.TRUE);

            assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}