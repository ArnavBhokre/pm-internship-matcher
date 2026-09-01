package com.internship.matcher.controller;

import com.internship.matcher.dto.request.InternshipRequest;
import com.internship.matcher.dto.response.ApiResponse;
import com.internship.matcher.model.entity.Internship;
import com.internship.matcher.service.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internships")
@RequiredArgsConstructor
public class InternshipController {

    private final InternshipService internshipService;

    /** Public: browse / search all active internships (used for the "discover" page) */
    @GetMapping
    public ResponseEntity<ApiResponse> list(@RequestParam(required = false) String location,
                                             @RequestParam(required = false) String sector,
                                             @RequestParam(required = false) String keyword) {
        var results = internshipService.search(location, sector, keyword);
        return ResponseEntity.ok(ApiResponse.ok("Internships fetched", results));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOne(@PathVariable Long id) {
        Internship internship = internshipService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("Internship fetched", internship));
    }

    /** Admin/seed endpoint to add a new internship posting */
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody InternshipRequest request) {
        Internship created = internshipService.create(request);
        return ResponseEntity.ok(ApiResponse.ok("Internship created", created));
    }
}
