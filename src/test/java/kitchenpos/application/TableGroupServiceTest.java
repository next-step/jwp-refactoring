package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    @DisplayName("create - 등록을 원하는 주문 테이블이 비어있거나, 1개밖에 없을경우 IllegalArugmentException이 발생한다.")
    void 등록을_원하는_주문_테이블이_비어있거나_1개밖에_없을경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("create - 등록을 원하는 주문 테이블과 등록된 주문 테이블의 개수가 틀릴경우 IllegalArgumentException이 발생한다.")
    void 등록을_원하는_주문_테이블과_등록된_주문_테이블의_개수가_틀릴경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("create - 등록된 주문 테이블의 상태가 비어있지 않거나, 이미 단체 지정이 되어있을 경우 IllegalArgumentException이 발생한다.")
    void 등록된_주문_테이블의_상태가_비어있지_않거나_이미_단체_지정이_되어있을_경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("create - 정상적인 단체지정 등록")
    void 정상적인_단체지정_등록() {

    }

    @Test
    @DisplayName("unGroup - 주문 테이블들의 고유 아이디를를 조회했을 때 주문 상태가 조리 이거나, 식사 일경우 IllegalArgumentException이 발생한다.")
    void 주문_테이블들의_고유_아이디를_조회했을_때_주문_상태가_조리_이거나_식사_일경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("unGroup - 정상적인 단체지정 해제")
    void 정상적인_단체지정_해제() {

    }
}