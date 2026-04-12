package com.example.demo.data_init;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.constant.EUserStatus;
import com.example.demo.constant.UserRole;
import com.example.demo.constant.UserStatus;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final CategoryRepository categoryRepository;
    private final FoodRepository foodRepository;
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    @Bean
    CommandLineRunner seedData() {
        return args -> {
            if(userRepository.count() <= 0){
                Users users = new Users();
                users.setEmail("0982407940ab@gmail.com");
                users.setPassword(passwordEncoder.encode("123"));
                users.setRoles(UserRole.ADMIN);
                users.setVerified(true);
                users.setStatus(EUserStatus.ACTIVE);
                users.setPhoneNumber("0385528994");
                users.setCreated_date(LocalDate.now());
                users.setUsername("yato");
                userRepository.save(users);

                Cart cart = new Cart();
                cart.setUsers(users);
                cartRepository.save(cart);
            }

            if (foodRepository.count() > 0) return;

            // ===== CATEGORY =====
            Category traSua = categoryRepository.save(Category.builder().name("Trà sữa").build());
            Category fastFood = categoryRepository.save(Category.builder().name("Fast food").build());
            Category bun = categoryRepository.save(Category.builder().name("Bún").build());
            Category com = categoryRepository.save(Category.builder().name("Cơm").build());
            Category sushi = categoryRepository.save(Category.builder().name("Sushi").build());
            Category nuoc = categoryRepository.save(Category.builder().name("Nước").build());
            Category lau = categoryRepository.save(Category.builder().name("Lẩu").build());
            Category monAnKem = categoryRepository.save(Category.builder().name("Món ăn kèm").build());
            Category banhMi = categoryRepository.save(Category.builder().name("Bánh mì").build());
            Category banhXeo = categoryRepository.save(Category.builder().name("Bánh xèo").build());
            // ===== FOOD LIST =====
            List<Food> foods = List.of(

                    buildFood("Trà sữa trân châu", "Trà sữa béo", 35000.0, 100,
                            "https://www.robins.vn/tra-sua-tran-chau-duong-den.html", traSua),

                    buildFood("Gà rán", "Gà giòn", 70000.0, 100,
                            "https://www.bluestone.com.vn/blogs/vao-bep/ga-ran-gion?srsltid=AfmBOory4lAXbMfSCR2uhnhj-pJYAjjv21tzkGJau-Oco9l2I4_p9WOk", fastFood),

                    buildFood("Bún bò Huế", "Bún bò cay", 50000.0, 100,
                            "https://shop.elmich.vn/bun-bo-hue?srsltid=AfmBOopuDY7T661z0U8Qnw_UYvzzDIeBEkiYMSShUhi8jucyBTSq1VXt", bun),

                    buildFood("Cơm tấm sườn", "Cơm sườn", 45000.0, 100,
                            "https://shopeefood.vn/ho-chi-minh/com-tam-co-5-com-tam-suon-nuong-mat-ong-ta-quang-buu?source_url=foody_ordernow_mobile", com),

                    buildFood("Hamburger", "Burger bò", 60000.0, 100,
                            "https://www.bachhoaxanh.com/kinh-nghiem-hay/cach-lam-hamburger-bo-kieu-my-ngon-nhu-ngoai-hang-1272718", fastFood),

                    buildFood("Pizza", "Pizza phô mai", 120000.0, 50,
                            "https://www.dienmayxanh.com/vao-bep/2-cach-lam-pizza-hai-san-pho-mai-bang-lo-nuong-va-noi-chien-04193?srsltid=AfmBOorPRlSAzpDoC_GLjiUWgvVQ8lZ-U0MPEKDyUnPean5_4VQY3PnX", fastFood),

                    buildFood("Khoai tây chiên", "Snack", 30000.0, 100,
                            "https://vietnhatplastic.com/tin-tuc/vao-bep/cach-lam-khoai-tay-chien-gion-tai-nha-ngon-dung-dieu", fastFood),

                    buildFood("Sushi", "Sushi Nhật", 120000.0, 50,
                            "https://byfood.b-cdn.net/api/public/assets/58874/content?optimizer=image", sushi),

                    buildFood("Cà phê sữa đá", "Cafe VN", 25000.0, 100,
                            "https://giacaphe.com/71322/ly-do-ca-phe-sua-da-viet-ngon-nhat-the-gioi/", nuoc),

                    buildFood("Matcha latte", "Matcha", 45000.0, 100,
                            "https://www.eatingbirdfood.com/iced-matcha-latte/", nuoc),

                    buildFood("Mì cay", "Mì cay Hàn", 50000.0, 100,
                            "https://shopeefood.vn/vung-tau/mi-cay-seoul-rach-dua-mi-cay-638-duong-30-4.zzgjhx", fastFood),

                    buildFood("Spaghetti", "Mì Ý", 80000.0, 100,
                            "https://lilluna.com/wp-content/uploads/2014/07/spaghetti-recipe-resize-9.jpg", fastFood),

                    buildFood("Salad", "Healthy", 40000.0, 100,
                            "https://www.primalkitchen.com/cdn/shop/articles/20240905221054-pk-greek-salad-0214-min.jpg?v=1725643684", monAnKem),

                    buildFood("Lẩu thái", "Lẩu cay", 200000.0, 30,
                            "https://i.ytimg.com/vi/jb0DiBpXQUo/maxresdefault.jpg", lau),

                    buildFood("Trà đào", "Trà mát", 35000.0, 100,
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUXIRQXh7eg2hffXUZ5qaAj_-kqtHlAOLPYQ&s", nuoc),

                    buildFood("Kem ly", "Tráng miệng", 30000.0, 100,
                            "https://cdn.tgdd.vn/2022/10/CookDishThumb/cach-trang-tri-kem-ly-kem-vien-xinh-xan-va-dep-mat-thumb-620x620.jpg", monAnKem),

                    buildFood("Bánh mì", "Bánh mì thịt VN", 20000.0, 100,
                            "https://media-cdn.tripadvisor.com/media/photo-m/1280/19/66/94/19/banh-mi-362.jpg", banhMi),

                    buildFood("Phở bò", "Phở truyền thống", 45000.0, 100,
                            "https://cdn.tgdd.vn/Files/2022/01/25/1412805/cach-nau-pho-bo-nam-dinh-chuan-vi-thom-ngon-nhu-hang-quan-202201250313281452.jpg", bun),

                    buildFood("Gỏi cuốn", "Cuốn tươi", 30000.0, 100,
                            "https://cdn2.fptshop.com.vn/unsafe/1920x0/filters:format(webp):quality(75)/2023_10_23_638336957766719361_cach-lam-goi-cuon.jpg", monAnKem),

                    buildFood("Bánh xèo", "Giòn ngon", 35000.0, 100,
                            "https://images.unsplash.com/photo-1604908177261-76d06cddc7e2", banhXeo)
            );

            // ===== ADD OPTION CHO 1 SỐ MÓN =====
            foods.forEach(food -> {

                if (food.getName().contains("Trà sữa")) {
                    addOption(food, "Size", List.of("M", "L"), List.of(0.0, 10000.0), true);
                }

                if (food.getName().contains("Gà")) {
                    addOption(food, "Combo", List.of("2 miếng", "4 miếng"), List.of(0.0, 40000.0), true);
                }

                if (food.getName().contains("Pizza")) {
                    addOption(food, "Size", List.of("S", "L"), List.of(0.0, 50000.0), true);
                }
            });

            foodRepository.saveAll(foods);

            System.out.println("🔥 Seed 20 foods DONE!");

        };
    }

    // ===== BUILD FOOD =====
    private Food buildFood(String name, String desc, Double price, Integer quantity,
                           String imageUrl, Category category) throws Exception {

        Food food = new Food();
        food.setName(name);
        food.setDescription(desc);
        food.setPrice(price);
        food.setQuantity(quantity);
        food.setCategory(category);


        FoodImage img = new FoodImage();
        img.setImageUrl(imageUrl);
        img.setPrimary(true);
        img.setFood(food);

        food.getImages().add(img);

        return food;
    }

    // ===== ADD OPTION =====
    private void addOption(Food food, String groupName,
                           List<String> names,
                           List<Double> prices,
                           boolean required) {

        GroupOption group = new GroupOption();
        group.setGroupName(groupName);
        group.setMinGroupOption(1);
        group.setMaxGroupOption(1);
        group.setIsRequired(required);
        group.setFood(food);

        for (int i = 0; i < names.size(); i++) {
            Option op = new Option();
            op.setOptionName(names.get(i));
            op.setExtraPrice(prices.get(i));
            op.setGroupOption(group);

            group.getItems().add(op);
        }

        food.getGroupOptions().add(group);
    }
}