package io.github.mrgsrylm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "CREATED_USER")
    protected String createdUser;

    @Column(name = "CREATED_AT")
    protected LocalDateTime createdAt;

    @Column(name = "UPDATED_USER")
    protected String updatedUser;

    @Column(name = "UPDATED_AT")
    protected LocalDateTime updatedAt;

   @PrePersist
   public void prePersist() {
       this.createdUser = getUsernameFromAuthentication();
       this.createdAt = LocalDateTime.now();
   }

   @PreUpdate
   public void preUpdate() {
       this.updatedUser = getUsernameFromAuthentication();
       this.updatedAt = LocalDateTime.now();
   }

   private String getUsernameFromAuthentication() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
           return userDetails.getUsername();
       }
       return "anonymousUser";
   }
}
