package com.greenacademy.productstore.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.greenacademy.productstore.dto.ProductDTO;
import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.repositories.ProductRepository;

@Service
public class ProductServices {
    private ProductRepository productRepository;

    public ProductServices(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAll(String name, String sku, Pageable pageable) {
        return productRepository.findAllByNameAndSku(name, sku, pageable);
    }

    public Page<ProductDTO> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product create(Product newProduct, MultipartFile productImage) {
        // Simpan file gambar dan dapatkan URL-nya
        String imageUrl = saveImage(productImage);
        newProduct.setImageUrl(imageUrl);
    
        // Simpan produk setelah semua field wajib sudah terisi
        return productRepository.save(newProduct);
    }
    
    private String saveImage(MultipartFile productImage) {
        try {

            String orginalFileName = productImage.getOriginalFilename();
            String extension = orginalFileName.substring(orginalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            String filePath = "src/main/resources/static/uploads/images/" + fileName;
            Path targetPath = Path.of(filePath);

            System.out.println(targetPath.toAbsolutePath()+" ini target path");

            Files.copy(productImage.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/images/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product update(Product product, MultipartFile productImage) {
        Product existingProduct = productRepository.findById(product.getId()).orElse(null);

        if(existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());

            if(!product.getSku().equals(product.getSku())) {
                existingProduct.setSku(product.getSku());
            }
            existingProduct.setSku(product.getSku());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDiscount(product.getDiscount());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setUpdated_at(Instant.now());

            if(productImage != null) {
                if(existingProduct.getImageUrl() != null) {
                    deleteImage(existingProduct.getImageUrl());
                }
                String imageUrl = saveImage(productImage);
                existingProduct.setImageUrl(imageUrl);
            }
            productRepository.save(existingProduct);
        }
        return existingProduct;
    }

    public void delete(Product product) {
        productRepository.delete(product);

        if(product.getImageUrl() != null) {
            deleteImage(product.getImageUrl());
        }       
    }

    private void deleteImage (String imageUrl) {
        String filePath = "src/main/resources/static"+imageUrl;
        Path targetPath = Path.of(filePath);
        try {
            Files.deleteIfExists(targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }
    

    public Iterable<Product> getByCategory(Integer id) {
        return productRepository.findByCategoryId(id);
    }
}
