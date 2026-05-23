package in.sp.main.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import in.sp.main.io.CategoryRequest;
import in.sp.main.io.CategoryResponse;
import in.sp.main.service.CategoryService;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping("/admin/category/addcategory")
	public ResponseEntity<CategoryResponse> addCategory(@RequestPart("category") String categoryString, @RequestPart("file") MultipartFile file){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		ObjectMapper objectMapper = new ObjectMapper();
		CategoryRequest request = null;
		try {
			request = objectMapper.readValue(categoryString,CategoryRequest.class);
			return new ResponseEntity<>(categoryService.addCategory(request,file,email),HttpStatus.CREATED);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occur while parsing the json"+e.getMessage());
		}

	}

	@GetMapping("/category/all")
	public List<CategoryResponse> getAllcCategory(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		return categoryService.getAllCategory(email);
	}

	@DeleteMapping("/admin/category/delete/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable String id){
		try {
			categoryService.deleteCategory(id);
			return new ResponseEntity<>("Category Deleted",HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Entity not Deleted"+e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/admin/category/update/{id}")
	public CategoryResponse updateCategory(@RequestPart("category") String categoryString , @PathVariable String id,@RequestPart("file") MultipartFile file ) {
		ObjectMapper objectMapper = new ObjectMapper();
		CategoryRequest request = null;
		try {
			request = objectMapper.readValue(categoryString,CategoryRequest.class);
			return categoryService.updatecategory(id,request,file);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occur while parsing the json"+e.getMessage());
		}

	}
}
