package kitchenpos.menu.exception;

public class MenuPriceGreaterThanProductsSumException extends IllegalArgumentException{
    public MenuPriceGreaterThanProductsSumException(){
        super("메뉴가격은 상품가격의 합보다 클 수 없습니다.");
    }
}
