package kitchenpos.ordering.acceptance;

import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.*;
import kitchenpos.ordering.domain.OrderLineItem;
import kitchenpos.ordering.domain.Ordering;
import kitchenpos.ordering.dto.OrderRequest;
import kitchenpos.ordering.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.utils.RestAssuredCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 | 한글명 | 영문명 | 설명 |
 | --- | --- | --- |
 | 상품       | product           | 메뉴를 관리하는 기준이 되는 데이터                   |
 | 메뉴 그룹   | menu group        | 메뉴 묶음, 분류                                  |
 | 메뉴       | menu              | 메뉴 그룹에 속하는 실제 주문 가능 단위                |
 | 메뉴 상품   | menu product       | 메뉴에 속하는 수량이 있는 상품                     |
 | 금액       | amount            | 가격 * 수량                                       |
 | 주문 테이블 | order table         | 매장에서 주문이 발생하는 영역                      |
 | 빈 테이블   | empty table        | 주문을 등록할 수 없는 주문 테이블                   |
 | 주문       | order             | 매장에서 발생하는 주문                              |
 | 주문 상태   | order status       | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다.       |
 | 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다.      |
 | 단체 지정    | table group       | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능     |
 | 주문 항목    | order line item   | 주문에 속하는 수량이 있는 메뉴                     |
 | 매장 식사    | eat in            | 포장하지 않고 매장에서 식사하는 것                   |

 // * 주문 - 주문테이블, 주문상태는?, *주문라인아이템(주문에 속하는 수량이 있는 메뉴)
 // * 주문라인아이템 - 주문, 메뉴
 // * 메뉴 - 메뉴그룹, 메뉴상품(제품)
 // * 메뉴상품 - 상품, 개수
 // * 상품
 */
@DisplayName("주문 관련 인수테스트")
public class OrderingAcceptanceTest extends AcceptanceTest {

    private ProductResponse 치킨상품;
    private int 치킨가격 = 13000;
    private ProductResponse 피자상품;
    private int 피자가격 = 17000;
    private MenuGroupResponse 피자치킨메뉴그룹;
    private MenuProduct 메뉴치킨상품;
    private MenuProduct 메뉴피자상품;
    private MenuResponse 피자치킨메뉴;
    private OrderLineItem 주문항목치킨;
    private OrderTable 주문테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        치킨상품 = RestAssuredCRUD
                .postRequest("/api/products", ProductRequest.of("치킨", BigDecimal.valueOf(치킨가격))).as(ProductResponse.class);
        피자상품 = RestAssuredCRUD
                .postRequest("/api/products", ProductRequest.of("피자", BigDecimal.valueOf(피자가격))).as(ProductResponse.class);
        메뉴치킨상품 = new MenuProduct(new Product(치킨상품.getId(), 치킨상품.getName(), 치킨상품.getPrice()), 2);
        메뉴피자상품 = new MenuProduct(new Product(피자상품.getId(), 피자상품.getName(), 피자상품.getPrice()), 1);
        피자치킨메뉴그룹 = RestAssuredCRUD
                .postRequest("/api/menu-groups", MenuGroupRequest.of("피자치킨메뉴그룹")).as(MenuGroupResponse.class);
        피자치킨메뉴 = RestAssuredCRUD.postRequest("/api/menus", MenuRequest.of(
                        "피자치킨",
                        BigDecimal.valueOf(26000+17000),
                        피자치킨메뉴그룹.getId(),
                        Arrays.asList(MenuProductRequest.of(메뉴치킨상품), MenuProductRequest.of(메뉴피자상품)))).as(MenuResponse.class);

        Long 주문ID = 1L;
        Long 테이블그룹ID = 1L;

        주문항목치킨 = new OrderLineItem(1L, 주문ID, 피자치킨메뉴.getId(), 3);
        주문테이블 = new OrderTable(1L, null, 5, false);
//        TableGroup 테이블그룹 = new TableGroup(테이블그룹ID, Arrays.asList(주문테이블));
    }

    @DisplayName("주문에 필요한 요소를 채워서 만든다.")
    @Test
    void 통합인수테스트_주문에_필요한_요소를_넣어서_주문을_넣는다() {
        // Given
        // 상품, 메뉴상품, 메뉴, 주문테이블, 주문항목 - 모두 준비되어있다.

        // When
        // 주문을 넣는다.
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), Arrays.asList(주문항목치킨));
        OrderResponse 주문결과 = RestAssuredCRUD.postRequest("menus", 주문요청).as(OrderResponse.class);

        // Then
        // 주문이 잘 만들어진다.
        assertThat(주문결과.getId()).isEqualTo(1L);
        assertThat(주문결과.getOrderTableId()).isEqualTo(주문테이블.getId());
        assertThat(주문결과.getOrderLineItems().stream()
                .map(orderLineItemResponse -> orderLineItemResponse.getId())).contains(주문항목치킨.getId());
        assertThat(주문결과.getId()).isEqualTo(주문항목치킨.getOrderId());
    }

}
