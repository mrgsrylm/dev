package io.github.mrgsrylm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HANDICRAFTS")
public class HandicraftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String image;

    private String description;

    private String materials;

    private String process;

    @OneToMany(mappedBy = "handicraft", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<ScrapEntity> scrapsCategory;

    public void setScrapsCategory(List<ScrapEntity> scrap) {
        this.scrapsCategory = scrap;
        scrapsCategory.forEach(scrapsCategory -> scrapsCategory.setHandicraft(this));
    }
}
