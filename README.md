### PETFOSTER ANNIMAL

# 🐾 PetFoster - Pet Adoption & Care Platform

PetFoster là một nền tảng toàn diện cho việc nhận nuôi thú cưng và chăm sóc thú cưng, bao gồm cửa hàng bán sản phẩm, hệ thống quản lý nhận nuôi và mạng xã hội cho người yêu thú cưng.

## 📋 Mục lục

- [Giới thiệu](#giới-thiệu)
- [Tính năng chính](#tính-năng-chính)
- [Kiến trúc hệ thống](#kiến-trúc-hệ-thống)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cài đặt và chạy dự án](#cài-đặt-và-chạy-dự-án)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [API Documentation](#api-documentation)
- [Đóng góp](#đóng-góp)

## 🎯 Giới thiệu

PetFoster là một hệ thống quản lý toàn diện cho việc nhận nuôi thú cưng và cung cấp các sản phẩm chăm sóc thú cưng. Hệ thống bao gồm:

- 🌐 **Frontend**: Ứng dụng web responsive được xây dựng với Next.js 14
- 🔧 **Backend**: RESTful API được phát triển với Spring Boot 3.2.7
- 🗄️ **Database**: SQL Server với Redis cho caching
- ☁️ **Storage**: AWS S3 cho lưu trữ hình ảnh

### 🌟 Điểm nổi bật

- 🤖 **AI-Powered**: Tích hợp trí tuệ nhân tạo cho matching thú cưng và tư vấn
- 📱 **Mobile-First**: Thiết kế responsive tối ưu cho mọi thiết bị
- ⚡ **Real-time**: Cập nhật trực tiếp với WebSocket và push notifications
- 🔒 **Secure**: Bảo mật đa lớp với JWT và OAuth2
- 🎨 **Modern UI/UX**: Giao diện hiện đại với animations mượt mà
- 🌍 **Scalable**: Kiến trúc microservices có thể mở rộng

## ✨ Tính năng chính

### 🏠 Dành cho người dùng

- 🐕 **Nhận nuôi thú cưng**: Duyệt và đăng ký nhận nuôi thú cưng với AI matching
- 🛒 **Cửa hàng trực tuyến**: Mua sắm sản phẩm chăm sóc thú cưng với giá tốt nhất
- 📸 **Mạng xã hội**: Chia sẻ hình ảnh và câu chuyện về thú cưng yêu thương
- 🤖 **Tư vấn AI**: Hỗ trợ tư vấn thông minh từ ChatGPT/Gemini
- 👤 **Quản lý hồ sơ**: Theo dõi lịch sử nhận nuôi và đơn hàng chi tiết
- 💳 **Thanh toán đa dạng**: Hỗ trợ mọi phương thức thanh toán phổ biến
- ⭐ **Đánh giá sản phẩm**: Đánh giá và chia sẻ kinh nghiệm chăm sóc thú cưng
- 🔍 **Tìm kiếm thông minh**: AI-powered search với bộ lọc thông minh
- ❤️ **Yêu thích**: Lưu trữ thú cưng và sản phẩm yêu thích
- 📱 **Thông báo realtime**: Cập nhật tức thời về đơn hàng và nhận nuôi
- 🏆 **Hệ thống điểm thưởng**: Tích lũy điểm qua mua sắm và hoạt động
- 📍 **Định vị GPS**: Tìm pet shop và trung tâm cứu hộ gần nhất
- 💬 **Chat trực tiếp**: Trao đổi với admin và cộng đồng pet lovers
- 📊 **Thống kê cá nhân**: Theo dõi chi tiêu và hoạt động chăm sóc thú cưng

### 🛠️ Dành cho quản trị viên

- 📈 **Dashboard thông minh**: Analytics và insights chi tiết về hệ thống
- 🐾 **Quản lý thú cưng**: CRUD operations với AI health monitoring
- 📦 **Quản lý sản phẩm**: Inventory management với auto-reorder
- 🚚 **Quản lý đơn hàng**: Tracking và xử lý đơn hàng tự động
- 👥 **Quản lý người dùng**: User management với role-based access
- 📝 **Quản lý đánh giá**: Moderation và response management
- 💰 **Quản lý tài chính**: Revenue tracking và báo cáo chi tiết
- 📧 **Email marketing**: Gửi newsletter và thông báo hàng loạt
- 🔒 **Bảo mật**: Monitoring và audit logs toàn diện
- 📊 **Báo cáo thống kê**: Export reports với multiple formats
- ⚙️ **Cấu hình hệ thống**: Settings và customization options
- 🎯 **Quản lý banner**: Dynamic content và promotion management

### 🎨 Tính năng đặc biệt

- 🧠 **AI Object Detection**: Nhận diện thú cưng qua camera với TensorFlow.js
- 💬 **Real-time Chat**: WebSocket messaging với emoji và file sharing
- 🗺️ **Google Maps**: Tích hợp bản đồ với directions và nearby services
- 📱 **PWA Ready**: Offline support và native app experience
- 🌐 **Đa ngôn ngữ**: i18n support cho Vietnamese và English
- 📱 **Responsive Design**: Mobile-first design cho mọi thiết bị
- 🔔 **Push Notifications**: Browser notifications cho updates quan trọng
- 🎵 **Sound Effects**: Audio feedback cho interactions
- 🌙 **Dark Mode**: Theme switching cho trải nghiệm tối ưu
- 🚀 **Performance**: Lazy loading và code splitting tối ưu
- 🔐 **Security**: JWT authentication với refresh token
- 📸 **Image Processing**: Auto-resize và compression thông minh
- 🎨 **Animation**: Smooth transitions với Framer Motion
- 📱 **Gesture Support**: Touch gestures cho mobile experience

## 🏗️ Kiến trúc hệ thống

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │    Database     │
│   (Next.js)     │◄──►│  (Spring Boot)  │◄──►│  (SQL Server)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         │              ┌─────────────────┐              │
         │              │     Redis       │              │
         └──────────────┤    (Cache)      ├──────────────┘
                        └─────────────────┘
                                 │
                        ┌─────────────────┐
                        │     AWS S3      │
                        │  (File Storage) │
                        └─────────────────┘
```

## 🛠️ Công nghệ sử dụng

### Frontend

- ⚛️ **Framework**: Next.js 14 với App Router
- 📝 **Language**: TypeScript
- 🎨 **Styling**: Tailwind CSS
- 🗃️ **State Management**: Redux Toolkit
- 🌐 **HTTP Client**: Axios với TanStack Query
- 🧩 **UI Components**: Material-UI, Custom Components
- 🗺️ **Maps**: Google Maps API
- 🤖 **AI/ML**: TensorFlow.js (COCO-SSD)
- 🔐 **Authentication**: Firebase Auth + JWT
- 💳 **Payment**: Multiple payment gateways
- ⚡ **Real-time**: WebSocket

### Backend

- 🍃 **Framework**: Spring Boot 3.2.7
- ☕ **Language**: Java 17
- 🗄️ **Database**: SQL Server
- 🚀 **Cache**: Redis
- 🛡️ **Security**: Spring Security + OAuth2
- ☁️ **File Storage**: AWS S3
- 📧 **Email**: Spring Boot Mail
- 📋 **Documentation**: Spring Boot Actuator
- 🔧 **Build Tool**: Maven

### DevOps & Tools

- 🐳 **Containerization**: Docker & Docker Compose
- 📚 **Version Control**: Git
- 📦 **Package Manager**: npm (Frontend), Maven (Backend)

## 🚀 Cài đặt và chạy dự án

### Yêu cầu hệ thống

- 🟢 Node.js 18+
- ☕ Java 17+
- 🔧 Maven 3.6+
- 🗄️ SQL Server
- 🚀 Redis
- 🐳 Docker & Docker Compose (optional)

### 1. Clone repository

```bash
git clone https://github.com/your-username/petfoster.git
cd petfoster
```

### 2. Setup Backend

```bash
cd petfoster_animal_backend

# Cài đặt dependencies
mvn clean install

# Khởi động Redis (sử dụng Docker)
docker-compose up -d

# Cấu hình database
# Tạo file src/main/resources/application.properties với nội dung:
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=petfoster
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Cấu hình AWS S3
aws.s3.bucket=your-bucket-name
aws.s3.region=your-region
aws.s3.access-key=your-access-key
aws.s3.secret-key=your-secret-key

# Khởi động ứng dụng
mvn spring-boot:run
```

Backend sẽ chạy trên: `http://localhost:8080`

### 3. Setup Frontend

```bash
cd petfoster_animal_frontend

# Cài đặt dependencies
npm install

# Cấu hình environment variables
# Tạo file .env.local với nội dung:
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_FIREBASE_API_KEY=your_firebase_api_key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your_auth_domain
NEXT_PUBLIC_GOOGLE_MAPS_API_KEY=your_google_maps_api_key

# Khởi động development server
npm run dev
```

Frontend sẽ chạy trên: `http://localhost:3000`

### 4. Build cho production

#### Backend

```bash
cd petfoster_animal_backend
mvn clean package
java -jar target/petfoster-0.0.1-SNAPSHOT.war
```

#### Frontend

```bash
cd petfoster_animal_frontend
npm run build
npm start
```

## 📁 Cấu trúc dự án

### Frontend Structure

```
petfoster_animal_frontend/
├── src/
│   ├── app/                    # App Router (Next.js 14)
│   │   ├── (admin)/           # Admin routes
│   │   ├── (header-dynamic)/  # Public routes with dynamic header
│   │   └── (header-fill)/     # Routes with filled header
│   ├── components/            # React components
│   │   ├── animations/        # Animation components
│   │   ├── common/           # Shared components
│   │   ├── pages/            # Page-specific components
│   │   └── products-and-pets/ # Product & pet components
│   ├── apis/                 # API service functions
│   ├── redux/                # Redux store & slices
│   ├── hooks/                # Custom React hooks
│   ├── utils/                # Utility functions
│   ├── data/                 # Static data & constants
│   └── styles/               # Global styles
├── public/                   # Static assets
└── package.json
```

### Backend Structure

```
petfoster_animal_backend/
├── src/main/java/com/hoangphi/
│   ├── controller/           # REST Controllers
│   │   ├── admin/           # Admin endpoints
│   │   ├── auth/            # Authentication
│   │   ├── pets/            # Pet management
│   │   └── products/        # Product management
│   ├── entity/              # JPA Entities
│   │   ├── User.java        # User entity
│   │   ├── Pet.java         # Pet entity
│   │   ├── Product.java     # Product entity
│   │   └── social/          # Social features entities
│   ├── service/             # Business logic
│   │   ├── impl/           # Service implementations
│   │   └── admin/          # Admin services
│   ├── repository/          # Data access layer
│   ├── request/             # Request DTOs
│   ├── response/            # Response DTOs
│   └── config/              # Configuration classes
├── src/main/resources/
│   ├── application.properties
│   └── templates/           # Email templates
└── pom.xml
```
## 🤝 Demo
![image](https://github.com/user-attachments/assets/f0983eec-9b36-4489-8d89-a11548c1b2b8)

![image](https://github.com/user-attachments/assets/000fb5cf-0619-438e-8eaa-79eb5bf4d498)

![image](https://github.com/user-attachments/assets/d3fbd3b7-9052-4c52-be28-d75ded2d6243)

![image](https://github.com/user-attachments/assets/d810da83-0a6c-4533-a37b-c3deddce5028)

![image](https://github.com/user-attachments/assets/9302011d-e9ba-4565-b08b-40fd0fb55b34)

![image](https://github.com/user-attachments/assets/b11d429b-1dc8-4227-8952-07d88bbf5351)

![image](https://github.com/user-attachments/assets/69270ef3-c0be-4e61-b23b-879e3c783285)

![image](https://github.com/user-attachments/assets/46164533-3e30-4e61-b34c-819c4b3a6c2e)

![image](https://github.com/user-attachments/assets/69eb8229-a52f-4f8e-a6db-31c43dfbd80d)

![image](https://github.com/user-attachments/assets/93406357-c2a1-4cad-9ee6-9cddc0dcece4)

![image](https://github.com/user-attachments/assets/a6750537-7963-4e5c-8119-1d069e58fd1c)

![image](https://github.com/user-attachments/assets/f812dcc9-31de-4c98-92c4-f1c43c131545)

![image](https://github.com/user-attachments/assets/3c670322-cb25-4e75-8369-2ed7d0df50c7)

![image](https://github.com/user-attachments/assets/9007dcd6-aac8-4c44-a6a9-65486b7d6c1d)

![image](https://github.com/user-attachments/assets/9d0df00b-f237-46c7-94b5-d27633ba0c3b)

![image](https://github.com/user-attachments/assets/6f4ed6cf-7c67-4c0f-81a0-f784e26c7250)

![image](https://github.com/user-attachments/assets/96a4860a-5f11-4e98-bcaa-9c1d5f012770)

![image](https://github.com/user-attachments/assets/6d246568-82ac-4f4e-9db2-a4326f25f8c5)

![image](https://github.com/user-attachments/assets/a0a8155f-26a8-4f40-8e06-f61cbc02dfa0)

![image](https://github.com/user-attachments/assets/ffe33906-0d44-45ad-8347-3713a23d4ef8)

![image](https://github.com/user-attachments/assets/4257ad25-34f1-4d83-b07d-5cbe3dabaf43)

![image](https://github.com/user-attachments/assets/c41bd5e5-9811-46ee-b272-bfd49d1b31e6)

![image](https://github.com/user-attachments/assets/af840662-05b1-48fb-bd40-e68e8f0dab81)

![image](https://github.com/user-attachments/assets/c6648fdc-6c0d-4cc5-bd06-cbb9271fcf42)

![image](https://github.com/user-attachments/assets/2b4c089f-d5ad-43b5-b13f-5b4df401eb8d)
## 📚 API Documentation

### 🔐 Authentication Endpoints

```
POST /api/auth/login          # 🚪 User login
POST /api/auth/register       # ➕ User registration
POST /api/auth/refresh        # 🔄 Refresh token
POST /api/auth/forgot-password # 🔑 Password reset
```

### 🐾 Pet Management

```
GET    /api/pets              # 📋 Get all pets
GET    /api/pets/{id}         # 🔍 Get pet by ID
POST   /api/pets              # ➕ Create new pet (Admin)
PUT    /api/pets/{id}         # ✏️ Update pet (Admin)
DELETE /api/pets/{id}         # 🗑️ Delete pet (Admin)
POST   /api/pets/{id}/adopt   # ❤️ Submit adoption request
```

### 🛒 Product Management

```
GET    /api/products          # 📦 Get all products
GET    /api/products/{id}     # 🔍 Get product by ID
POST   /api/products          # ➕ Create product (Admin)
PUT    /api/products/{id}     # ✏️ Update product (Admin)
DELETE /api/products/{id}     # 🗑️ Delete product (Admin)
```

### 📊 Order Management

```
GET    /api/orders            # 📋 Get user orders
POST   /api/orders            # 🛍️ Create new order
GET    /api/orders/{id}       # 🔍 Get order details
PUT    /api/orders/{id}/status # 📈 Update order status (Admin)
```

### 📱 Social Features

```
GET    /api/posts             # 📰 Get all posts
POST   /api/posts             # ✍️ Create new post
POST   /api/posts/{id}/like   # 👍 Like/unlike post
POST   /api/posts/{id}/comment # 💬 Comment on post
```

## 🧪 Testing

### Backend Testing

```bash
cd petfoster_animal_backend
mvn test
```

### Frontend Testing

```bash
cd petfoster_animal_frontend
npm run test
```

## 🔧 Environment Variables

## 🤝 Đóng góp

Chúng tôi hoan nghênh mọi đóng góp từ cộng đồng! Để đóng góp:

1. Fork repository
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

### Coding Standards

- **Frontend**: ESLint + Prettier
- **Backend**: Checkstyle + SpotBugs
- **Commit Messages**: Conventional Commits
- **Testing**: Minimum 80% coverage

## 📄 License

Dự án này được phát hành dưới [MIT License](LICENSE).

## 👥 Tác giả

- **Hoàng Phi** - _Lead Developer_ - [GitHub](https://github.com/hoangphi)

## 📞 Liên hệ

- Email: contact@petfoster.com
- Website: https://petfoster.com
- Documentation: https://docs.petfoster.com

## 🙏 Lời cảm ơn

- Spring Boot Community
- Next.js Team
- TensorFlow.js Contributors
- All open source libraries used in this project

---

⭐ Nếu dự án này hữu ích, hãy cho tôi một star trên GitHub!

























