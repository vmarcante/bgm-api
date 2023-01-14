package br.com.api.bgm.controllers.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {
	protected ResponseEntity<Object> response(Object obj) {
        if (obj == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
        else if(obj instanceof Exception) {
			Exception ex = ((Exception) obj);
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

}
