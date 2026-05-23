package in.sp.main.Exceptions;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalExceptionHandler{
	@ExceptionHandler({categoryNotFoundException.class,itemNotExistException.class})
	public ResponseEntity<Map<String,Object>> EmailAllReadyExist(Exception e){
		Map<String,Object> map = new HashMap<>();
		map.put("timestamp",LocalDateTime.now());
		map.put("message",e.getMessage());
		return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({EmailAllReadyExistException.class,GstAllreadyExistExceptipn.class,UserAlreadyExistEception.class,CategoryAlreadyExistsException.class,ItemAlReadyExistException.class})
	public ResponseEntity<Map<String,Object>> AllReadyExist(Exception e){
		Map<String,Object> map = new HashMap<>();
		map.put("timestamp",LocalDateTime.now());
		map.put("message",e.getMessage());
		return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
	}
}
