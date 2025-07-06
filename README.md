#  Saathi - Safe Route Founder

Saathi – Safe Route Finder is a web app  built using Java Spring Boot that empowers users—especially women & child and daily commuters—to make safer travel decisions. By leveraging geolocation, risk mapping, and user-generated testimonialsa.

## Tech Stack

| Layer             | Technology                          |
|------------------|--------------------------------------|
| Language          | Java 17                             |
| Framework         | Spring Boot                         |
| Build Tool        | Maven                               |
| Security          | Spring Security + JWT               |
| APIs Used         | OpenRouteService, Nominatim OSM     |
| Email             | JavaMailSender                      |
| Containerization  | Docker                              |
| Validation        | Jakarta Bean Validation (JSR-380)   |
| Frontend          | HTML, CSS, JavaScript               |
---


## Features

- **Smart Route Generation**  
  Generate routes using [OpenRouteService](https://openrouteservice.org/) and classify route segments by safety levels.

- **Geo-Location & Reverse Geocoding**  
  Fetch human-readable location data using **Nominatim (OpenStreetMap)** API.

- **Risk-Aware Area Classification**  
  Zones marked as Red, Yellow, or Safe based on user testimonials.

- **User Testimonials**  
  Users can submit, update, and delete safety experiences from specific areas.

- **Place Management**  
  Save areas by geocoded addresses and risk classification.

- **Emergency Contacts**  
  Add and manage trusted contacts for quick alerts.

- **SOS Event Logging**  
  Allows users to trigger SOS with location, timestamp, and status.

- **Authentication & Security**  
  Includes JWT-based auth, secure password reset via **JavaMailSender**, and user profile management.


## API Documentation
  Want to know in detail?? [Dcumentation](https://western-aluminum-170.notion.site/Saathi-App-Documentation-21fe44bc5a7f80d38857f80537adb39e).

## API Endpoints (Summary)

### User

- `PUT /api/v1/user/update` – Update profile  
- `POST /api/v1/user/update/password` – Change password

### Authentication

- `POST /api/v1/auth/forgot` – Send verification email  
- `POST /api/v1/auth/reset` – Reset password

### Emergency Contacts

- `POST /api/v1/emergency/contact-add` – Add contact  
- `GET /api/v1/emergency/contacts` – Get all contacts  
- `GET /api/v1/emergency/contact/{id}` – Get specific contact  
- `PUT /api/v1/emergency/contact-update/{id}` – Update contact  
- `DELETE /api/v1/emergency/contact-delete/{id}` – Delete contact

### Places

- `GET /api/v1/place` – Get all saved places  
- `POST /api/v1/place` – Save a new location  
- `GET /api/v1/place/highRisk?state={state}` – Get high-risk zones by state

### Testimonials

- `POST /api/v1/testimonials` – Submit testimonial  
- `GET /api/v1/testimonials/places/{placeId}` – Get testimonials for place  
- `GET /api/v1/testimonials/me` – Get user's own testimonials  
- `PUT /api/v1/testimonials/{id}` – Update testimonial  
- `DELETE /api/v1/testimonials/{id}` – Delete testimonial

### SOS

- `POST /api/v1/sos` – Send SOS with location & message

### Safe Route

- `POST /api/v1/route/safe` – Generate safe route between locations  
- `GET /api/v1/route/risk/summary?state={state}` – Get risk distribution summary

---


### Installation & Running

1. Clone the repository:

```bash
git clone https://github.com/lakshaybxt/restaurant-review-platform.git
cd restaurant-review-platform
```
### Start all required services using Docker Compose:

```bash
docker-compose up
```
This command will launch Elasticsearch, Kibana, Keycloak, and your Spring Boot backend.

Alternatively, to run the Spring Boot application separately:

```bash
./mvnw spring-boot:run
```

## Contributing

Pull requests are welcome. If you find bugs or want to suggest features, feel free to open an issue or create a PR.


Thank you for checking out our Saathi! If you have any questions or feedback, feel free to reach out!
