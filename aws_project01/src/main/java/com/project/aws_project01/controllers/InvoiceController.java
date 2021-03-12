package com.project.aws_project01.controllers;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.project.aws_project01.models.Invoice;
import com.project.aws_project01.models.UrlResponse;
import com.project.aws_project01.repositories.InvoiceRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Value("${aws.s3.bucket.invoice.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;
    private final InvoiceRepository invoiceRepository;

    @PostMapping
    public ResponseEntity<UrlResponse> createInvoiceUrl() {
        UrlResponse urlResponse = new UrlResponse();
        Instant expirationTime = Instant.now().plus(Duration.ofMinutes(5));
        String processId = UUID.randomUUID().toString();

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, processId)
                .withMethod(HttpMethod.PUT).withExpiration(Date.from(expirationTime));

        urlResponse.setExpirationTime(expirationTime.getEpochSecond());
        urlResponse.setUrl(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString());

        return ResponseEntity.ok(urlResponse);
    }

    @GetMapping
    public ResponseEntity<Iterable<Invoice>> findAll() {
        return ResponseEntity.ok(invoiceRepository.findAll());
    }

    @GetMapping("/customerName/{name}")
    public ResponseEntity<Iterable<Invoice>> findByCustomerName(@PathVariable("name") String customerName) {
        return ResponseEntity.ok(invoiceRepository.findAllByCustomerName(customerName));
    }

}
