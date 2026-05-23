package in.sp.main.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import in.sp.main.io.ItemRequest;
import in.sp.main.io.ItemResponse;
import in.sp.main.service.ItemService;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;

	@PostMapping("/admin/additem")
	public ResponseEntity<ItemResponse> addItem(@RequestPart("item") String itemString, @RequestPart("file") MultipartFile file) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		ObjectMapper mapper = new ObjectMapper();
		ItemRequest request = null;
		try {
			request = mapper.readValue(itemString,ItemRequest.class);
			return new ResponseEntity<>(itemService.addItem(request, file,email),HttpStatus.CREATED);
		}catch(HttpMessageNotReadableException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error occured while the json");
		}
	}

	@GetMapping("/items")
	public List<ItemResponse> getAllItems(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		return itemService.getAllItems(email);
		}

	@DeleteMapping("/admin/items/delete/{itemId}")
	public ResponseEntity<String> deleteItem(@PathVariable String itemId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
			itemService.deleteItem(itemId,email);
			return new ResponseEntity<>("Deleted",HttpStatus.NO_CONTENT);

	}
}
