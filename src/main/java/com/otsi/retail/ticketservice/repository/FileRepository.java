package com.otsi.retail.ticketservice.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.ticketservice.entities.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Serializable> {

	Optional<FileEntity> findByFileName(String fileName);

	Optional<FileEntity> findByTicketsId(Long ticketId);

}
