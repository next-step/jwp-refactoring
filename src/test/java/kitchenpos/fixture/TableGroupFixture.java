//package kitchenpos.fixture;
//
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static kitchenpos.fixture.TableFixture.회사A_테이블1;
//import static kitchenpos.fixture.TableFixture.회사A_테이블2;
//
//public class TableGroupFixture {
//
//    public static final TableGroup 회사A_단체_테이블 = create(1L, LocalDateTime.now(), Arrays.asList(회사A_테이블1, 회사A_테이블2));
//
//    private TableGroupFixture() {
//        throw new UnsupportedOperationException();
//    }
//
//    public static TableGroup create(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setId(id);
//        tableGroup.setCreatedDate(createdDate);
//        tableGroup.setOrderTables(orderTables);
//
//        return tableGroup;
//    }
//}
