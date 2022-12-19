package kitchenpos.exception;

public class MenuError {
    private static final String PREFIX = "[ERROR] ";

    public static final String INVALID_PRICE = PREFIX + "메뉴 가격은 0 이상 전체 상품 가격 합계 이하로 설정 가능합니다.";
    public static final String REQUIRED_MENU_GROUP = PREFIX + "메뉴 그룹이 필수로 지정되어야 합니다.";
    public static final String NOT_FOUND = PREFIX + "메뉴를 찾을 수 없습니다.";

    
    private MenuError() {
        
    }
}
