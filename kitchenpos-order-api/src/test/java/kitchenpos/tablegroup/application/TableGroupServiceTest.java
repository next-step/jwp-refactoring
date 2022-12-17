package kitchenpos.tablegroup.application;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItemRequest;
import static kitchenpos.order.domain.OrderMenuTestFixture.generateOrderMenu;
import static kitchenpos.order.domain.OrderTestFixture.generateOrder;
import static kitchenpos.ordertable.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static kitchenpos.tablegroup.domain.TableGroupTestFixture.generateTableGroup;
import static kitchenpos.tablegroup.domain.TableGroupTestFixture.generateTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuTestFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private TableGroupService tableGroupService;

    private Product 불고기버거;
    private MenuProduct 불고기버거상품;
    private MenuGroup 햄버거단품;
    private Menu 불고기버거단품;
    private OrderMenu 불고기버거단품주문상품;
    private OrderLineItemRequest 불고기버거세트주문요청;
    private Order 주문;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderTable 주문테이블C;
    private OrderTable 주문테이블D;


    @BeforeEach
    void setUp() {
        불고기버거 = ProductTestFixture.generateProduct("불고기버거", BigDecimal.valueOf(4000L));
        불고기버거상품 = MenuProductTestFixture.generateMenuProduct(불고기버거, 1L);
        햄버거단품 = MenuGroupTestFixture.generateMenuGroup("햄버거단품");
        불고기버거단품 = MenuTestFixture.generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(4000L), 햄버거단품, singletonList(불고기버거상품));
        불고기버거단품주문상품 = generateOrderMenu(불고기버거단품);
        불고기버거세트주문요청 = generateOrderLineItemRequest(불고기버거단품.getId(), 2);
        주문테이블A = generateOrderTable(1L, 5, true);
        주문테이블B = generateOrderTable(2L, 4, false);
        주문테이블C = generateOrderTable(3L, 5, true);
        주문테이블D = generateOrderTable(4L, 4, true);
        주문 = generateOrder(주문테이블B, singletonList(불고기버거세트주문요청.toOrderLineItem(불고기버거단품주문상품)));
    }

    @DisplayName("주문 테이블들에 대해 단체를 설정한다.")
    @Test
    void createTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(Arrays.asList(주문테이블C.getId(), 주문테이블D.getId()));
        TableGroup 단체 = generateTableGroup(1L, Arrays.asList(주문테이블C, 주문테이블D));
        given(orderTableRepository.findById(주문테이블C.getId())).willReturn(Optional.of(주문테이블A));
        given(orderTableRepository.findById(주문테이블D.getId())).willReturn(Optional.of(주문테이블B));
        given(tableGroupRepository.save(any())).willReturn(단체);

        // when
        TableGroupResponse saveTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> Assertions.assertThat(주문테이블C.findTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> Assertions.assertThat(주문테이블D.findTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> Assertions.assertThat(주문테이블C.isEmpty()).isFalse(),
                () -> Assertions.assertThat(주문테이블D.isEmpty()).isFalse(),
                () -> Assertions.assertThat(saveTableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("생성하고자 하는 단체에 속하는 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableSizeIsSmallerThenTwo() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(singletonList(주문테이블A.getId()));
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.주문_테이블은_2개_이상여야함.getErrorMessage());
    }

    @DisplayName("등록되지 않은 주문 테이블을 가진 단체는 생성할 수 없다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableIsNotExists() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()));
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderTableRepository.findById(주문테이블B.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage());
    }

    @DisplayName("단체 생성 시, 해당 단체에 속할 주문 테이블이 없으면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableIsNotEmpty() {
        // given
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.주문_테이블_집합은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("단체 생성 시, 다른 단체에 포함된 주문 테이블이 있다면 예외가 발생한다.")
    @Test
    void createTableGroupThrowErrorWhenOrderTableInOtherTableGroup() {
        // given
        TableGroup 단체A = generateTableGroup(1L, Arrays.asList(주문테이블C, 주문테이블D));
        TableGroupRequest tableGroupRequest = generateTableGroupRequest(Arrays.asList(주문테이블B.getId(), 주문테이블C.getId()));
        given(orderTableRepository.findById(주문테이블B.getId())).willReturn(Optional.of(주문테이블B));
        given(orderTableRepository.findById(주문테이블C.getId())).willReturn(Optional.of(주문테이블C));
        given(tableGroupRepository.save(any())).willReturn(단체A);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(tableGroupRequest))
                .withMessage(ErrorCode.단체_그룹_지정되어_있음.getErrorMessage());
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroup 단체 = generateTableGroup(2L, Arrays.asList(주문테이블A, 주문테이블B));
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        given(tableGroupRepository.findById(단체.getId())).willReturn(Optional.of(단체));
        given(orderTableRepository.findAllByTableGroupId(단체.getId())).willReturn(Arrays.asList(주문테이블A, 주문테이블B));
        given(orderRepository.findAllByOrderTableIdIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()))).willReturn(singletonList(주문));

        // when
        tableGroupService.ungroup(단체.getId());

        // then
        assertAll(
                () -> Assertions.assertThat(주문테이블A.findTableGroupId()).isNull(),
                () -> Assertions.assertThat(주문테이블B.findTableGroupId()).isNull()
        );
    }
}
