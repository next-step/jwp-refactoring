package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }


    @Test
    @DisplayName("create - 등록을 원하는 주문에 주문 항목이 비어있으면 IllegalArgumentException 이 발생한다.")
    void 등록을_원하는_주문에_주문_항목이_비어있으면_IllegalArgumentException_이_발생한다() {

    }

    @Test
    @DisplayName("create - 등록을 원하는 주뭉항목이 DB에 전부 존재하는지 확인하여 전부 존재하지 않으면 IllegalArgumentException이 발생한다.")
    void 등록을_원하는_주뭉항목이_DB에_전부_존재하는지_확인하여_전부_존재하지_않으면_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("create - 주문 테이블이 존재하는지 확인하고, 없으면 IllegalArgumentException이 발생한다.")
    void 주문_테이블이_존재하는지_확인하고_없으면_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("create - 주문 테이블이 빈 테이블일 경우 IllegalArgumentException이 발생한다.")
    void 주문_테이블이_빈_테이블일_경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 등록")
    void 정상적인_주문_테이블_등록() {

    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 전체 조회")
    void 정상적인_주문_테이블_전체_조회() {

    }

    @Test
    @DisplayName("changeOrderStatus - 변경을 원하는 주문을 DB에서 가져오고, 없으면 IllegalArgumentException이 발생한다.")
    void 변경을_원하는_주문을_DB에서_가져오고_없으면_IllegalArgumentException이_발생한다(){
    }

    @Test
    @DisplayName("changeOrderStatus - 주문의 상태가 계산 완료이고, 변경하려는 상태도 계산완료일 경우 IllegalArgumentException 이 발생한다.")
    void 주문의_상태가_계산_완료이고_변경하려는_상태도_계산완료일_경우_IllegalArgumentException이_발생한다() {

    }

    @Test
    @DisplayName("changeOrderStatus - 정상적인 주문 상태 변경")
    void 정상적인_주문_상태_변경() {

    }
}