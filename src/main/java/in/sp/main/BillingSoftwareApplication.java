package in.sp.main;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingSoftwareApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(BillingSoftwareApplication.class, args);

		KeyGenerator keygen = KeyGenerator.getInstance("HmacSHA256");
		SecretKey sk = keygen.generateKey();
		System.out.println(Base64.getEncoder().encodeToString(sk.getEncoded()));

	}

}
