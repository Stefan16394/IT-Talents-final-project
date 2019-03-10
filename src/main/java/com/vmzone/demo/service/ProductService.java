package com.vmzone.demo.service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vmzone.demo.dto.AddCharacteristicDTO;
import com.vmzone.demo.dto.AddProductDTO;
import com.vmzone.demo.dto.EditProductDTO;
import com.vmzone.demo.dto.ListProduct;
import com.vmzone.demo.dto.ListProductBasicInfo;
import com.vmzone.demo.dto.ListReview;
import com.vmzone.demo.exceptions.BadCredentialsException;
import com.vmzone.demo.exceptions.ResourceAlreadyExistsException;
import com.vmzone.demo.exceptions.ResourceDoesntExistException;
import com.vmzone.demo.models.Category;
import com.vmzone.demo.models.Characteristic;
import com.vmzone.demo.models.Photo;
import com.vmzone.demo.models.Product;
import com.vmzone.demo.repository.CategoryRepository;
import com.vmzone.demo.repository.CharacteristicsRepository;
import com.vmzone.demo.repository.PhotoRepository;
import com.vmzone.demo.repository.ProductRepository;
import com.vmzone.demo.repository.ReviewRepository;

@Service
public class ProductService {
	private static final int SMALL_QUANTITY_INDICATOR = 10;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private CharacteristicsRepository characteristicRepository;

	public Product addProduct(AddProductDTO product) throws ResourceDoesntExistException {
		Category category = null;
		try {
			category = this.categoryRepository.findById(product.getCategoryId()).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "There is no such category");
		}
		
		Product newProduct = new Product(category, product.getTitle(), product.getInformation(), product.getInStock(),
				product.getDelivery(), product.getQuantity(), product.getInSale(), product.getDetailedInformation());

		return this.productRepository.save(newProduct);
	}

	public long addCharacteristicForProduct(long productId, AddCharacteristicDTO characteristic)
			throws ResourceAlreadyExistsException, ResourceDoesntExistException {
		Characteristic checkExists = this.characteristicRepository.findNameOfCharacteristicForProduct(productId,
				characteristic.getValue());
		if (checkExists != null) {
			throw new ResourceAlreadyExistsException(
					"There is already a characteristic with that name for this product");
		}
		try {
			this.productRepository.findById(productId).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException("There is no such category");
		}

		Characteristic newCharacteristic = new Characteristic(this.productRepository.findById(productId).get(),
				characteristic.getName(), characteristic.getValue());
		this.characteristicRepository.save(newCharacteristic);
		return newCharacteristic.getCharacteristicsId();
	}

	public void removeCharacteristicForProduct(long prodId, long charactId) throws ResourceDoesntExistException {

		Characteristic characteristic = this.characteristicRepository.findCharacteristicForProduct(prodId, charactId);
		if (characteristic == null) {
			throw new ResourceDoesntExistException("There is no such characteristic for this product");
		}
		characteristic.setIsDeleted(1);
		this.characteristicRepository.save(characteristic);
	}

	public List<ListReview> getReviewsForProduct(long id) {
		return this.reviewRepository.findReviewsForProduct(id).stream()
				.filter(rev -> rev.getIsDeleted() == 0)
				.map(review -> new ListReview(review.getReviewId(), review.getReview(), review.getRating()))
				.collect(Collectors.toList());
	}

	public List<AddCharacteristicDTO> getCharacteristicsForProduct(long id) {
		return this.characteristicRepository.findAll().stream().filter(
				charact -> charact.getCharacteristicsId() != null && charact.getProduct().getProductId().equals(id))
				.map(charact -> new AddCharacteristicDTO(charact.getName(), charact.getValue()))
				.collect(Collectors.toList());
	}

	public ListProduct getAllInfoForProduct(long id) throws BadCredentialsException {
		Product p = null;
		try {
			p = this.productRepository.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new BadCredentialsException("There is no such product");
		}
		if(p.getIsDeleted() == 1) {
			throw new BadCredentialsException("There product has been deleted!");
		}

		List<ListReview> reviews = getReviewsForProduct(id);
		List<AddCharacteristicDTO> characteristics = getCharacteristicsForProduct(id);
		List<Photo> photos = photoRepository.findPhotosForProductById(p.getProductId());
			
		ListProduct info = new ListProduct(p.getProductId(), p.getTitle(), p.getInformation(), p.getInStock(),
				p.getDelivery(), p.getDetailedInformation(), p.getRating());
		if (!reviews.isEmpty()) {
			info.fillReviews(reviews);
		}
		if (!characteristics.isEmpty()) {
			info.fillCharacteristics(characteristics);
		}
		if(!photos.isEmpty()) {
			info.setPhotos(photos);
		}

		return info;

	}

	public List<Product> getAllproducts(long id) {
		List<Long> ids = this.categoryService.getLeafCategories(id).stream().map(c -> c.getId())
				.collect(Collectors.toList());
		return this.getProductPresentInCategories(ids);
	}

	public List<Product> getProductPresentInCategories(List<Long> categoriesIds) {

		return this.productRepository.getProductsPresentInCategories(categoriesIds);
	}

	public List<ListProductBasicInfo> getAllProductsWithSmallQuantity() {
		return this.productRepository.getAllProductsWithSmallQuantity(SMALL_QUANTITY_INDICATOR).stream()
				.filter(prod -> prod.getIsDeleted() == 0)
				.map(product -> {
					List<Photo> photos = photoRepository.findPhotosForProductById(product.getProductId());	
					Photo photo = photos.isEmpty() ? null : photos.get(0);
				    return new ListProductBasicInfo(product.getProductId(), product.getTitle(), product.getPrice(), product.getDate(), photo);
				})
				.collect(Collectors.toList());
	}

	public void removeProductById(long id) throws ResourceDoesntExistException {
		Product product = null;
		try {
			product = this.productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}

		product.setIsDeleted(1);
		this.productRepository.save(product);
	}

	public Product editProduct(long id, EditProductDTO editedProduct) throws ResourceDoesntExistException {
		Product product = null;
		try {
			product = this.productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Product doesn't exist");
		}

		Category cat = null;
		try {
			cat = this.categoryRepository.findById(editedProduct.getCategoryId()).get();
		} catch (NoSuchElementException e) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "Category doesn't exist");
		}
		product.setCategory(cat);
		product.setDelivery(editedProduct.getDelivery());
		product.setDetailedInformation(editedProduct.getDetailedInformation());
		product.setInformation(editedProduct.getInformation());
		product.setInSale(editedProduct.getInSale());
		product.setInStock(editedProduct.getInStock());
		product.setIsDeleted(editedProduct.getIsDeleted());
		product.setQuantity(editedProduct.getQuantity());
		product.setTitle(editedProduct.getTitle());

		return this.productRepository.save(product);
	}

	public List<ListProduct> getAllproducts() {
		return this.productRepository.findAll().stream()
				.filter(product -> product.getProductId() != null && product.getIsDeleted() == 0)
				.map(product -> {
					try {
						return getAllInfoForProduct(product.getProductId());
					} catch (BadCredentialsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}

				}).collect(Collectors.toList());
	}

	public List<ListProductBasicInfo> getAllproducts(String sortBy, Long categoryId, Double minPrice, Double maxPrice) {
		List<Product> products = categoryId == null
				? this.productRepository.findAll().stream().filter(p->p.getIsDeleted()==0)
						.collect(Collectors.toList())
				: this.getAllproducts(categoryId);
		
		Comparator<ListProductBasicInfo> comparator = (p1,p2)->p1.getDate().compareTo(p2.getDate());
				if (sortBy != null) {
					switch (sortBy) {
					case "newest":
						comparator =(p1,p2)->  p1.getDate().compareTo(p2.getDate());
						break;
					case "oldest":
						comparator =(p1,p2)-> p2.getDate().compareTo(p1.getDate());
						break;
					case "ascending price":
						comparator =(p1,p2)-> Double.compare(p1.getPrice(), p2.getPrice());
						break;
					case "descending price":
						comparator =(p1,p2)-> Double.compare(p2.getPrice(), p1.getPrice());
						break;
					case "ascending alphabetic":
						comparator =(p1,p2)-> p1.getTitle().compareTo(p2.getTitle());
						break;
					case "descending alphabetic":
						comparator =(p1,p2)-> p2.getTitle().compareTo(p1.getTitle());
						break;
					default:
						comparator= (p1,p2)->  p1.getDate().compareTo(p2.getDate());
					}
				}
		return products.stream()
			.filter(p -> minPrice == null || maxPrice == null || p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
			.map(p->{
				List<Photo> photos = photoRepository.findPhotosForProductById(p.getProductId());
				Photo photo = photos.isEmpty()? null :photos.get(0);
				return new ListProductBasicInfo(p.getProductId(), p.getTitle(), p.getPrice(), p.getDate(), photo);
			})

		.sorted(comparator).collect(Collectors.toList());
	}

	public List<ListProductBasicInfo> sortCharacteristicsByColour(String sortBy, Long categoryId) {
		return this.characteristicRepository.findCharacteristicWithColourAndValue(sortBy).stream()
				.filter(charact -> categoryId == null || charact.getProduct().getCategory().getCategoryId().equals(categoryId))
				.map(charact -> {
				List<Photo> photos = photoRepository.findPhotosForProductById(charact.getProduct().getProductId());
				Photo photo = photos.isEmpty()? null :photos.get(0);
				return new ListProductBasicInfo(charact.getProduct().getProductId(),
						charact.getProduct().getTitle(), charact.getProduct().getPrice(),
						charact.getProduct().getDate(),photo);
		        })
				.collect(Collectors.toList());
	}

	public List<ListProductBasicInfo> sortCharacteristicsBySize(String sortBy, Long categoryId) {
		return this.characteristicRepository.findCharacteristicWithSizeAndValue(sortBy).stream().filter(
				charact -> categoryId == null || charact.getProduct().getCategory().getCategoryId().equals(categoryId))
				.map(charact -> {
					List<Photo> photos = photoRepository.findPhotosForProductById(charact.getProduct().getProductId());
					Photo photo = photos.isEmpty()? null :photos.get(0);
					return new ListProductBasicInfo(charact.getProduct().getProductId(),
							charact.getProduct().getTitle(), charact.getProduct().getPrice(),
							charact.getProduct().getDate(),photo);
			        })
					.collect(Collectors.toList());
	}

	public void calculateRating() throws ResourceDoesntExistException {
		List<ListProduct> products = getAllproducts();

		if (products.isEmpty()) {
			throw new ResourceDoesntExistException(HttpStatus.NOT_FOUND, "There are no products");
		}

		for (ListProduct p : products) {
			List<ListReview> reviews = getReviewsForProduct(p.getId());
			p.fillReviews(reviews);
			Product prod = this.productRepository.findById(p.getId()).get();
			if (reviews.isEmpty()) {
				prod.setRating(Double.valueOf(0));
			} else {
				int sum = 0;
				for (ListReview r : reviews) {
					sum += r.getRating();
				}
				Double average = (double) (sum / reviews.size());
				prod.setRating(average);
			}
			this.productRepository.save(prod);
		}

	}

	public List<ListProductBasicInfo> searchPrice(double min, double max) {

		List<Product> products = this.productRepository.searchPrice(min, max);
		List<ListProductBasicInfo> result = new LinkedList<>();

		for (Product p : products) {
			ListProductBasicInfo prod = new ListProductBasicInfo(p.getProductId(), p.getTitle(), p.getPrice(),
					p.getDate(),null);
			result.add(prod);
		}
		return result;
	}

	public List<ListProductBasicInfo> searchTitle(String str) {
		str = "%" + str + "%";
		List<Product> products = this.productRepository.search(str);
		List<ListProductBasicInfo> result = new LinkedList<>();

		for (Product p : products) {
			ListProductBasicInfo prod = new ListProductBasicInfo(p.getProductId(), p.getTitle(), p.getPrice(),
					p.getDate(),null);
			result.add(prod);
		}
		return result;
	}

}
