//package kitchenpos.application;
//
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.TableGroup;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//
//import static kitchenpos.application.TableServiceTest.이번_테이블;
//import static kitchenpos.application.TableServiceTest.일번_테이블;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class TableGroupServiceTest {
//    public static final TableGroup 그룹_테이블 = new TableGroup();
//
//    static {
//        그룹_테이블.setOrderTables(Arrays.asList(일번_테이블, 이번_테이블));
//    }
//
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private OrderTableDao orderTableDao;
//    @Mock
//    private TableGroupDao tableGroupDao;
//    @InjectMocks
//    TableGroupService tableGroupService;
//
//    @Test
//    @DisplayName("테이블 병합")
//    void create() {
//        // given
//        given(orderTableDao.findAllByIdIn(any()))
//                .willReturn(Arrays.asList(일번_테이블, 이번_테이블));
//        given(tableGroupDao.save(any()))
//                .willReturn(그룹_테이블);
//        given(orderTableDao.save(any()))
//                .willReturn(일번_테이블);
//        // when
//        final TableGroup tableGroup = tableGroupService.create(그룹_테이블);
//        // then
//        assertThat(tableGroup)
//                .isInstanceOf(TableGroup.class);
//    }
//
//    @Test
//    @DisplayName("테이블 분리")
//    void ungroup() {
//        // given
//        given(orderTableDao.findAllByTableGroupId(그룹_테이블.getId()))
//                .willReturn(Arrays.asList(일번_테이블, 이번_테이블));
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
//                .willReturn(false);
//        given(orderTableDao.save(any()))
//                .willReturn(일번_테이블);
//        // when
//        tableGroupService.ungroup(그룹_테이블.getId());
//        // then
//        assertThat(일번_테이블.getTableGroupId()).isNull();
//    }
//}
