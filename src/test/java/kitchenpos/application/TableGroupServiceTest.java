package kitchenpos.application;

import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.application.fixture.TableGroupFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 일번_테이블;
    private OrderTable 이번_테이블;
    private OrderTable 등록된_일번_테이블;
    private OrderTable 등록된_이번_테이블;
    private OrderTable 비어있지_않은_테이블;
    private TableGroup 등록한_단체;

    @BeforeEach
    void setup() {
        일번_테이블 = OrderTableFixture.create(1L, null, 2, true);
        이번_테이블 = OrderTableFixture.create(2L, null, 4, true);
        등록된_일번_테이블 = OrderTableFixture.create(1L, 1L, 2, false);
        등록된_이번_테이블 = OrderTableFixture.create(2L, 1L, 4, false);
        비어있지_않은_테이블 = OrderTableFixture.create(3L, null, 4, false);

        등록한_단체 = TableGroupFixture.create(1L, Arrays.asList(등록된_일번_테이블, 등록된_이번_테이블));
    }

    @DisplayName("단체 지정 테스트")
    @Nested
    class TestCreateTableGroup {
        @DisplayName("단체 지정 확인")
        @Test
        void 단체_지정_확인() {
            // given
            TableGroup 등록_요청_데이터 = new TableGroup();
            등록_요청_데이터.setOrderTables(Arrays.asList(일번_테이블, 이번_테이블));

            given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(일번_테이블, 이번_테이블));
            given(tableGroupDao.save(any(TableGroup.class))).willReturn(등록한_단체);
            given(orderTableDao.save(일번_테이블)).willReturn(등록된_일번_테이블);
            given(orderTableDao.save(이번_테이블)).willReturn(등록된_이번_테이블);

            // when
            TableGroup 등록된_단체 = tableGroupService.create(등록_요청_데이터);

            // then
            assertThat(등록된_단체).isEqualTo(등록한_단체);
        }

        @DisplayName("단체에 등록하는 테이블이 없음")
        @Test
        void 단체에_등록하는_테이블이_없음() {
            // given
            TableGroup 등록_요청_데이터 = new TableGroup();
            등록_요청_데이터.setOrderTables(null);

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> tableGroupService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체에 등록하는 테이블이 2개 미만임")
        @Test
        void 단체에_등록하는_테이블이_2개_미만임() {
            // given
            TableGroup 등록_요청_데이터 = new TableGroup();
            등록_요청_데이터.setOrderTables(Collections.singletonList(일번_테이블));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> tableGroupService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("중복된 테이블을 등록 요청")
        @Test
        void 중복된_테이블을_등록_요청() {
            // given
            TableGroup 등록_요청_데이터 = new TableGroup();
            등록_요청_데이터.setOrderTables(Arrays.asList(일번_테이블, 일번_테이블));

            given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(일번_테이블));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> tableGroupService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있지 않은 테이블을 등록 요청")
        @Test
        void 비어있지_않은_테이블을_등록_요청() {
            // given
            TableGroup 등록_요청_데이터 = new TableGroup();
            등록_요청_데이터.setOrderTables(Arrays.asList(일번_테이블, 비어있지_않은_테이블));

            given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(일번_테이블, 비어있지_않은_테이블));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> tableGroupService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체로 지정된 테이블을 요청")
        @Test
        void 이미_단체로_지정된_테이블을_요청() {
            // given
            TableGroup 등록_요청_데이터 = new TableGroup();
            등록_요청_데이터.setOrderTables(Arrays.asList(일번_테이블, 등록된_이번_테이블));

            given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(일번_테이블, 등록된_이번_테이블));

            // when
            ThrowableAssert.ThrowingCallable 등록_요청 = () -> tableGroupService.create(등록_요청_데이터);

            // then
            assertThatThrownBy(등록_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 해지 테스트")
    @Nested
    class TestUngroupTableGroup {
        @DisplayName("단체 해지 확인")
        @Test
        void 단체_해지_확인() {
            // given
            given(orderTableDao.findAllByTableGroupId(any(Long.TYPE)))
                    .willReturn(Arrays.asList(등록된_일번_테이블, 등록된_이번_테이블));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .willReturn(false);

            // when
            tableGroupService.ungroup(1L);
        }

        @DisplayName("조리 또는 식사 상태의 주문을 가진 그룹 해지")
        @Test
        void 조리_또는_식사_상태의_주문을_가진_그룹_해지() {
            // given
            given(orderTableDao.findAllByTableGroupId(any(Long.TYPE)))
                    .willReturn(Arrays.asList(등록된_일번_테이블, 등록된_이번_테이블));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .willReturn(true);

            // when
            ThrowableAssert.ThrowingCallable 해지_요청 = () -> tableGroupService.ungroup(1L);

            // then
            assertThatThrownBy(해지_요청).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
