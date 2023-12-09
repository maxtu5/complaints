package com.complaints.dao;

import com.complaints.model.Complaint;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


import java.util.UUID;

public interface ComplaintRepository extends ReactiveCrudRepository<Complaint, UUID> {

}
