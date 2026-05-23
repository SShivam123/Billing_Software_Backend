package in.sp.main.Exceptions;

public class UserAlreadyExistEception extends RuntimeException {
	public UserAlreadyExistEception(String message) {
		super(message);
	}
}
