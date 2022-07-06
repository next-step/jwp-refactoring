package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
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
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 생성")
    @Nested
    class 단체_지정_생성 {
        @DisplayName("단체 지정을 생성할 수 있다.")
        @Test
        void 단체_지정_성공() {
            // given
            OrderTableRequest 주문_테이블_요청1 = OrderTableRequest.of(1L, 5, false);
            OrderTableRequest 주문_테이블_요청2 = OrderTableRequest.of(2L, 3, false);

            TableGroupRequest 단체_지정_요청 = TableGroupRequest.from(Arrays.asList(주문_테이블_요청1, 주문_테이블_요청2));

            OrderTable 주문_테이블1 = new OrderTable(주문_테이블_요청1.getId(), 주문_테이블_요청1.getNumberOfGuests(),
                    주문_테이블_요청1.isEmpty());
            OrderTable 주문_테이블2 = new OrderTable(주문_테이블_요청2.getId(), 주문_테이블_요청2.getNumberOfGuests(),
                    주문_테이블_요청2.isEmpty());
            List<OrderTable> 주문_테이블 = Arrays.asList(주문_테이블1, 주문_테이블2);
            TableGroup 단체_지정 = TableGroup.of(1L, new OrderTables(주문_테이블));

            given(orderTableRepository.findAllByIdIn(anyList())).willReturn(주문_테이블);
            given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_지정);

            // when
            TableGroupResponse 단체_지정_결과 = tableGroupService.create(단체_지정_요청);

            // then
            assertAll(() -> assertThat(단체_지정_결과.getId()).isNotNull(),
                    () -> assertThat(단체_지정_결과.getOrderTables()).hasSize(2));
        }

        @DisplayName("단체 지정 실패")
        @Nested
        class 단체_지정_실패 {
            @DisplayName("하나 이하의 주문 테이블을 포함한 주문의 경우 단체 지정할 수 없다.")
            @Test
            void 하나_이하의_주문_테이블을_포함한_단체_지정_생성() {
                // given
                OrderTableRequest 주문_테이블_요청 = OrderTableRequest.of(1L, 5, true);
                TableGroupRequest 단체_지정_요청 = TableGroupRequest.from(Collections.singletonList(주문_테이블_요청));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청)).isInstanceOf(
                        IllegalArgumentException.class);
            }

            @DisplayName("존재하지 않는 주문 테이블이 포함된 주문의 경우 단체 지정할 수 없다.")
            @Test
            void 존재하지_않는_주문_테이블이_포함된_단체_지정() {
                // given
                Long 존재하지_않는_주문_테이블_아이디 = 99999L;
                OrderTableRequest 주문_테이블_요청1 = OrderTableRequest.of(1L, 5, true);
                OrderTableRequest 주문_테이블_요청2 = OrderTableRequest.of(존재하지_않는_주문_테이블_아이디, 3, true);

                TableGroupRequest 단체_지정_요청 = TableGroupRequest.from(Arrays.asList(주문_테이블_요청1, 주문_테이블_요청2));

                OrderTable 주문_테이블 = new OrderTable(주문_테이블_요청1.getId(), 주문_테이블_요청1.getNumberOfGuests(),
                        주문_테이블_요청1.isEmpty());

                given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Collections.singletonList(주문_테이블));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청)).isInstanceOf(
                        IllegalArgumentException.class);
            }

            @DisplayName("중복된 주문 테이블을 포함한 주문은 단체 지정할 수 없다.")
            @Test
            void 중복된_주문_테이블이_포함된_주문_단체_지정() {
                // given
                OrderTableRequest 주문_테이블_요청1 = OrderTableRequest.of(1L, 5, true);
                OrderTableRequest 주문_테이블_요청2 = OrderTableRequest.of(1L, 3, true);

                TableGroupRequest 단체_지정_요청 = TableGroupRequest.from(Arrays.asList(주문_테이블_요청1, 주문_테이블_요청2));

                OrderTable 주문_테이블 = new OrderTable(주문_테이블_요청1.getId(), 주문_테이블_요청1.getNumberOfGuests(),
                        주문_테이블_요청1.isEmpty());

                given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Collections.singletonList(주문_테이블));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청)).isInstanceOf(
                        IllegalArgumentException.class);
            }

            @DisplayName("빈 주문 테이블이 아닌 주문 테이블을 포함한 주문을 단체 지정할 수 없다.")
            @Test
            void 빈_주문_테이블이_아닌_주문_테이블을_포함한_주문_단체_지정() {
                // given
                OrderTableRequest 주문_테이블_요청1 = OrderTableRequest.of(1L, 5, true);
                OrderTableRequest 주문_테이블_요청2 = OrderTableRequest.of(1L, 3, false);

                TableGroupRequest 단체_지정_요청 = TableGroupRequest.from(Arrays.asList(주문_테이블_요청1, 주문_테이블_요청2));

                OrderTable 주문_테이블1 = new OrderTable(주문_테이블_요청1.getId(), 주문_테이블_요청1.getNumberOfGuests(),
                        주문_테이블_요청1.isEmpty());
                OrderTable 주문_테이블2 = new OrderTable(주문_테이블_요청2.getId(), 주문_테이블_요청2.getNumberOfGuests(),
                        주문_테이블_요청2.isEmpty());

                given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청)).isInstanceOf(
                        IllegalArgumentException.class);
            }

            @DisplayName("이미 단체 지정된 주문 테이블이 포함된 주문의 경우 단체 지정할 수 없다.")
            @Test
            void 이미_단체_지정된_주문_테이블을_포함한_주문은_단체_지정_불가() {
                OrderTableRequest 주문_테이블_요청1 = OrderTableRequest.of(1L, 5, true);
                OrderTableRequest 주문_테이블_요청2 = OrderTableRequest.of(1L, 3, false);

                TableGroupRequest 단체_지정_요청 = TableGroupRequest.from(Arrays.asList(주문_테이블_요청1, 주문_테이블_요청2));

                OrderTable 주문_테이블1 = new OrderTable(주문_테이블_요청1.getId(), 주문_테이블_요청1.getNumberOfGuests(),
                        주문_테이블_요청1.isEmpty());
                OrderTable 주문_테이블2 = new OrderTable(주문_테이블_요청2.getId(), 1L, 주문_테이블_요청2.getNumberOfGuests(),
                        주문_테이블_요청2.isEmpty());

                given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));

                // when / then
                assertThatThrownBy(() -> tableGroupService.create(단체_지정_요청)).isInstanceOf(
                        IllegalArgumentException.class);
            }
        }
    }

    @DisplayName("단체 지정 삭제")
    @Nested
    class 단체_지정_삭제 {
        @DisplayName("단체 지정을 삭제할 수 있다.")
        @Test
        void 단체_지정_삭제_성공() {
            Long 테이블_그룹_아이디 = 1L;

            OrderTable 주문_테이블1 = new OrderTable(1L, 3, false);
            OrderTable 주문_테이블2 = new OrderTable(2L, 5, false);

            TableGroup 테이블_그룹 = new TableGroup(테이블_그룹_아이디, new OrderTables(Arrays.asList(주문_테이블1, 주문_테이블2)));

            given(tableGroupRepository.findById(eq(테이블_그룹_아이디))).willReturn(Optional.of(테이블_그룹));
            given(orderService.existOrderBeforeCompletion(anyList())).willReturn(Boolean.FALSE);

            tableGroupService.ungroup(테이블_그룹_아이디);

            assertThat(주문_테이블1.getTableGroupId()).isNull();
        }

        @DisplayName("주문상태가 계산 완료가 아니면 삭제할 수 없다.")
        @Test
        void 주문상태가_계산_완료가_아닌_경우_삭제_불가() {
            TableGroup 테이블_그룹 = TableGroup.of(1L, OrderTables.create());

            given(tableGroupRepository.findById(eq(1L))).willReturn(Optional.of(테이블_그룹));
            given(orderService.existOrderBeforeCompletion(anyList())).willReturn(Boolean.TRUE);

            assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
