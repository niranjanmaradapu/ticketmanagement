/**
 * 
 */
package com.otsi.retail.ticketservice.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.ticketservice.entities.CommentEntity;

/**
 * @author Sudheer.Swamy
 *
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Serializable> {

}
