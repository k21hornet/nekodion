package com.konekokonekone.nekodion.ingest.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class IngestSecretFilter extends OncePerRequestFilter {

    private static final String SECRET_HEADER = "X-Ingest-Secret";

    @Value("${ingest.shared-secret}")
    private String sharedSecret;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var header = request.getHeader(SECRET_HEADER);

        if (header == null || !MessageDigest.isEqual(
                header.getBytes(StandardCharsets.UTF_8),
                sharedSecret.getBytes(StandardCharsets.UTF_8)
        )) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
