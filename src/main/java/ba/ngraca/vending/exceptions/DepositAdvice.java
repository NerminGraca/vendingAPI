package ba.ngraca.vending.exceptions;

import ba.ngraca.vending.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DepositAdvice {

    @ResponseBody
    @ExceptionHandler(DepositException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    MessageResponse depositHandler(DepositException e) {
        return new MessageResponse(e.getMessage());
    }
}
