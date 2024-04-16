package com.evs.voter.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8081",value = "EVSCheckElectionResult")
public interface EVSResultApiClient {

	@GetMapping("admin/result/checkResult/{electionCode}")
	public ResponseEntity<String> checkResults(@PathVariable String electionCode);
}
