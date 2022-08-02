package com.otsi.retail.ticketservice.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.ticketservice.entities.FeedBackEntity;

public interface FeedBackRepository extends JpaRepository<FeedBackEntity, Serializable> {

}
