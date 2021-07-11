package kitchenpos.domain;

import static kitchenpos.domain.MenuProductTest.*;
import static kitchenpos.domain.ProductTest.*;

public class OrderLineItemDetailTest {
    public static OrderLineItemDetail 후라이드_주문내역_상세 = new OrderLineItemDetail(후라이드.getName(), 후라이드.getPrice(), MP1후라이드.getQuantity());
    public static OrderLineItemDetail 양념치킨_주문내역_상세 = new OrderLineItemDetail(양념치킨.getName(), 양념치킨.getPrice(), MP2양념치킨.getQuantity());
    public static OrderLineItemDetail 반반치킨_주문내역_상세 = new OrderLineItemDetail(반반치킨.getName(), 반반치킨.getPrice(), MP3반반치킨.getQuantity());
    public static OrderLineItemDetail 순살치킨_주문내역_상세 = new OrderLineItemDetail(순살치킨.getName(), 순살치킨.getPrice(), MP6순살치킨.getQuantity());
}
