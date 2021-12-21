package kitchenpos.domain;

public class MenuPricePolicy {
    private MenuPricePolicy() {
    }

    public static void validate(Menu menu) {
        long price = menu.getPrice();
        long totalProductPrice = menu.getTotalProductPrice();
        if (price > totalProductPrice) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 총 합보다 클 수 없습니다");
        }
    }
}
