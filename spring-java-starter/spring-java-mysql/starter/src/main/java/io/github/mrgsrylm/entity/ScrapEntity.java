package io.github.mrgsrylm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.mrgsrylm.enums.ScrapType;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SCARPS")
public class ScrapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private ScrapType type;

    private String image;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "handicraftId", referencedColumnName = "id")
    private HandicraftEntity handicraft;
}
