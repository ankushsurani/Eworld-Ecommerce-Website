package com.eworld.controllers.admin;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eworld.entities.Category;
import com.eworld.entities.Order;
import com.eworld.entities.Product;
import com.eworld.entities.ProductImage;
import com.eworld.entities.User;
import com.eworld.helper.FileImageUpload;
import com.eworld.helper.Msg;
import com.eworld.services.CategoryService;
import com.eworld.services.OrderService;
import com.eworld.services.ProductImageService;
import com.eworld.services.ProductService;
import com.eworld.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
	
	@Value("${product.image}")
	private String path;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private OrderService orderService;
	
	@ModelAttribute
	public void currentUser(Principal principal, Model model) {
		if(principal != null) {
			User user = this.userService.findByEmail(principal.getName());
				model.addAttribute("currentUser", user);
		}
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		model.addAttribute("title","Dashboard - Eworld");
		
		try {
			
			List<User> users = this.userService.getAllUser();
			model.addAttribute("users", users);
			
			List<Category> categories = this.categoryService.getAllCategories();
			model.addAttribute("categories", categories);
			
			List<Product> products = this.productService.getAllProducts();
			model.addAttribute("products", products);
			
			model.addAttribute("product", new Product());
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Msg("Something Went Wrong!!", "alert-danger"));
		}
		
		return "admin/dashboard";
	}
	
	@PostMapping("/add-category")
	public String addCategory(@ModelAttribute("category") Category category, HttpSession session, Model model) {
		
		model.addAttribute("title","Add category - Eworld");
		
		try {
			
			this.categoryService.saveCategory(category);
			session.setAttribute("message", new Msg("Category added successfully", "alert-success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Msg("Something Went Wrong!!", "alert-danger"));
		}
		
		return "redirect:/admin/dashboard";
		
	}
	
	@PostMapping("/add-product")
	public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, @RequestParam("catId") int catId, @RequestParam("product_images") List<MultipartFile> files, HttpSession session, Model model) {
		
		model.addAttribute("title","Add product - Eworld");
		
		try {
			
			if(result.hasErrors()) {
				model.addAttribute("product", product);
				
				List<User> users = this.userService.getAllUser();
				model.addAttribute("users", users);
				
				List<Product> products = this.productService.getAllProducts();
				model.addAttribute("products", products);
				
				List<Category> categories = this.categoryService.getAllCategories();
				model.addAttribute("categories", categories);
				
				session.setAttribute("message", new Msg("Please Fill valid details of Product", "alert-warning"));
				return "admin/dashboard";
			}
			
			Category category = this.categoryService.getCategory(catId);
			
			product.setCategory(category);
			
			this.productService.saveProduct(product);
			
			try {
				
				for(MultipartFile file : files) {
					String fileName = FileImageUpload.uploadImage(path, file);
					ProductImage productImage = new ProductImage();
					productImage.setProductPic(fileName);
					productImage.setProduct(product);
					this.productImageService.saveImage(productImage);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("message", new Msg(e.getMessage(), "alert-danger"));
				return "redirect:/admin/dashboard";
			}
			
			session.setAttribute("message", new Msg("Product added successfully", "alert-success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Msg("Something Went Wrong!!", "alert-danger"));
		}
		
		return "redirect:/admin/dashboard";
		
	}
	
	
	@GetMapping("/current-orders")
	public String currentOrders(Model model, @RequestParam(name = "statusSelect", required = false) String status, HttpSession session) {
		model.addAttribute("title", "Orders - Eworld");
		
		try {
			
			if(status == null || status.equals("all")) {
				List<Order> orders = this.orderService.findOrderByStatus("Awaiting Payment", "Awaiting Pickup", "Partially Shipped");
				model.addAttribute("orders", orders);
			}
			else {
				List<Order> statusOrders = this.orderService.getOrderByStatus(status);
				model.addAttribute("orders", statusOrders);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Msg("Something Went Wrong!!", "alert-danger"));
		}
		
		return "admin/current_orders";
	}
	
	
	//change order status
	@PostMapping("/change-status/{orderId}")
	public String changeOrderStatus(@PathVariable("orderId") int orderId, @RequestParam("status") String status,
			@RequestParam(name = "deliveryDate", required = false) String deliveryDate, Model model, HttpSession session) {
		model.addAttribute("title", "Change Status - Eworld");
		
		try {
			
			Order order = this.orderService.findById(orderId);

			if(status.equals("Completed")) {
				order.setStatus(status);
				order.setDeliveryDate(new Date());
			}
			else {
				order.setStatus(status);
				SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = formatter.parse(deliveryDate);
					order.setDeliveryDate(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			
			this.orderService.saveOrder(order);
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Msg("Something Went Wrong!!", "alert-danger"));
		}
		
		return "redirect:/admin/current-orders";
		
	}

	
}
