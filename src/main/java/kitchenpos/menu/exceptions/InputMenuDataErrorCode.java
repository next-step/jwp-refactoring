package kitchenpos.menu.exceptions;

public enum InputMenuDataErrorCode {

    IT_CAN_NOT_INPUT_MENU_PRICE_LESS_THAN_ZERO("[ERROR]메뉴 가격은 0보다 작을 수 없습니다");

    private String errorMessage;

    InputMenuDataErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return this.errorMessage;
    }
}
