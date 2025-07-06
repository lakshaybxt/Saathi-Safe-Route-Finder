package com.saathi.saathi_be.domain.entity;

import com.saathi.saathi_be.domain.GeoLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sos_event")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SosEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String deviceStatus;

    @PrePersist
    public void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
