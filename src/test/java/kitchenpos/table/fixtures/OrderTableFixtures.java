package kitchenpos.table.fixtures;

import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeGuestNumberRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableSaveRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.util.Lists;

import java.math.BigDecimal;

import static kitchenpos.menu.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.menu.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.menu.fixtures.ProductFixtures.후라이드;
import static kitchenpos.table.fixtures.TableGroupFixtures.주문불가_5인_2인_그룹테이블;

/**
 * packageName : kitchenpos.fixtures
 * fileName : TableFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class OrderTableFixtures {
    public static OrderTableSaveRequest 주문불가_다섯명테이블요청() {
        return OrderTableSaveRequest.of(5, true);
    }

    public static OrderTableSaveRequest 주문불가_두명테이블요청() {
        return OrderTableSaveRequest.of(2, true);
    }

    public static OrderTableSaveRequest 주문가능_다섯명테이블요청() {
        return OrderTableSaveRequest.of(5, false);
    }

    public static OrderTableSaveRequest 주문가능_두명테이블요청() {
        return OrderTableSaveRequest.of(2, false);
    }

    public static OrderTable 주문가능_다섯명테이블() {
        return 주문가능_다섯명테이블요청().toEntity();
    }

    public static OrderTable 주문가능_두명테이블() {
        return 주문가능_두명테이블요청().toEntity();
    }

    public static OrderTable 주문불가_다섯명테이블() {
        return 주문불가_다섯명테이블요청().toEntity();
    }

    public static OrderTable 주문불가_두명테이블() {
        return 주문불가_두명테이블요청().toEntity();
    }

    public static OrderTable 그룹화된_테이블() {
        return 주문불가_다섯명테이블요청().toEntity()
                .groupBy(주문불가_5인_2인_그룹테이블());
    }

    public static OrderTable 주문이_완료되지_않은_테이블() {
        BigDecimal 메뉴가격 = new BigDecimal(32000);
        MenuProduct 양념치킨메뉴상품 = new MenuProduct(양념치킨(), 1L);
        MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드(), 1L);
        Menu 후라이드반양념반메뉴 = new Menu("후라이드반양념반메뉴", 메뉴가격, 메뉴그룹("반반메뉴").getId(), Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품));
        OrderLineItem 후라이드양념반두개 = new OrderLineItem(후라이드반양념반메뉴.getId(), 2L);
        Order order = new Order(주문가능_다섯명테이블(), Lists.newArrayList(후라이드양념반두개));

        return 주문불가_다섯명테이블요청().toEntity()
                .addOrder(order);
    }

    public static ChangeEmptyRequest 주문불가로_변경요청() {
        return new ChangeEmptyRequest(true);
    }

    public static ChangeEmptyRequest 주문가능으로_변경요청() {
        return new ChangeEmptyRequest(false);
    }

    public static ChangeGuestNumberRequest 다섯명으로_변경요청() {
        return new ChangeGuestNumberRequest(5);
    }

    public static ChangeGuestNumberRequest 두명으로_변경요청() {
        return new ChangeGuestNumberRequest(2);
    }

    public static ChangeGuestNumberRequest 사용자수_변경요청(Integer numberOfGuests) {
        return new ChangeGuestNumberRequest(numberOfGuests);
    }

    public static OrderTableRequest 테이블_그룹요청() {
        return new OrderTableRequest();
    }

    public static OrderTableRequest 테이블_그룹요청(Long tableId) {
        return new OrderTableRequest(tableId);
    }
}

