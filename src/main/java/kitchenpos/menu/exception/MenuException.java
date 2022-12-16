package kitchenpos.menu.exception;

public class MenuException extends RuntimeException{

    public MenuException(MenuExceptionType exceptionType){
        super(exceptionType.message);
    }

    public MenuException(String message){
        super(message);
    }

}
