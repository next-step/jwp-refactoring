//package kitchenpos.fixture;
//
//import java.math.BigDecimal;
//import kitchenpos.acceptance.MenuGroupAcceptanceTest;
//import kitchenpos.acceptance.ProductAcceptanceTest;
//import kitchenpos.domain.Menu;
//import kitchenpos.domain.MenuGroup;
//import kitchenpos.domain.MenuProduct;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.Product;
//import kitchenpos.domain.TableGroup;
//
//public class AcceptanceTestFixture {
//    public final MenuGroup 구이류;
//    public final MenuGroup 식사류;
//
//    public final Product 삼겹살;
//    public final Product 목살;
//    public final Product 김치찌개;
//    public final Product 공깃밥;
//
//    public final MenuProduct 돼지모듬_삼겹살;
//    public final MenuProduct 돼지모듬_목살;
//    public final MenuProduct 김치찌개정식_김치찌개;
//    public final MenuProduct 김치찌개정식_공깃밥;
//
//    public final Menu 돼지모듬;
//    public final Menu 김치찌개정식;
//
//    public final OrderLineItem 주문_항목1;
//    public final OrderLineItem 주문_항목2;
//    public final OrderLineItem 완료된_주문_항목;
//
//    public final OrderTable 테이블;
//    public final OrderTable 빈_테이블1;
//    public final OrderTable 빈_테이블2;
//    public final OrderTable 단체_지정_빈_테이블;
//    public final OrderTable 단체_지정_테이블1;
//    public final OrderTable 단체_지정_테이블2;
//
//    public final Order 주문;
//    public final Order 완료된_주문;
//
//    public final TableGroup 단체1;
//
//    public AcceptanceTestFixture() {
//        this.구이류 = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청("구이류").as(MenuGroup.class);
//        this.식사류 = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청("식사류").as(MenuGroup.class);
//
//        this.삼겹살 = ProductAcceptanceTest.상품_생성_요청("삼겹살", BigDecimal.valueOf(14000L)).as(Product.class);
//        this.목살 = ProductAcceptanceTest.상품_생성_요청("목살", BigDecimal.valueOf(15000L)).as(Product.class);
//        this.김치찌개 = ProductAcceptanceTest.상품_생성_요청("김치찌개", BigDecimal.valueOf(8000L)).as(Product.class);
//        this.공깃밥 = ProductAcceptanceTest.상품_생성_요청("공깃밥", BigDecimal.valueOf(1000L)).as(Product.class);
//
//        this.돼지모듬_삼겹살 = 돼지모듬_삼겹살;
//        this.돼지모듬_목살 = 돼지모듬_목살;
//        this.김치찌개정식_김치찌개 = 김치찌개정식_김치찌개;
//        this.김치찌개정식_공깃밥 = 김치찌개정식_공깃밥;
//
//        this.돼지모듬 = 돼지모듬;
//        this.김치찌개정식 = 김치찌개정식;
//
//        this.주문_항목1 = 주문_항목1;
//        this.주문_항목2 = 주문_항목2;
//        this.완료된_주문_항목 = 완료된_주문_항목;
//
//        this.테이블 = 테이블;
//        this.빈_테이블1 = 빈_테이블1;
//        this.빈_테이블2 = 빈_테이블2;
//        this.단체_지정_빈_테이블 = 단체_지정_빈_테이블;
//        this.단체_지정_테이블1 = 단체_지정_테이블1;
//        this.단체_지정_테이블2 = 단체_지정_테이블2;
//
//        this.주문 = 주문;
//        this.완료된_주문 = 완료된_주문;
//
//        this.단체1 = 단체1;
//    }
//}
