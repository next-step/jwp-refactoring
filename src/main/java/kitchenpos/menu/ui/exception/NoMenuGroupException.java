package kitchenpos.menu.ui.exception;

public class NoMenuGroupException extends RuntimeException{
    public NoMenuGroupException() {
        super("찾는 메뉴 그룹이 없습니다");
    }
}
