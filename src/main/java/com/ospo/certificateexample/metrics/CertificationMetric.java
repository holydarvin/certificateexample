package com.ospo.certificateexample.metrics;


import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;


@RestController
@RequiredArgsConstructor
public class CertificationMetric {

    private final MeterRegistry registry;
    private KeyStore keyStore;

    @PostConstruct
    public void initialize() {
        initKeyStore();
        Enumeration<String> aliases = null;
        try {
            aliases = keyStore.aliases();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        aliases.asIterator().forEachRemaining(this::createMetricForCertificate);

    }


    private void createMetricForCertificate(String certificateName) {


        Certificate certificate = null;
        try {
            certificate = keyStore.getCertificate(certificateName);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        if (certificate instanceof X509Certificate) {
            var dateSign = ((X509Certificate) certificate).getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            var daysRemaining = getDifferentDaysWithDateNow(dateSign);

            var nameMetric = "certificate.%s.days_remaining".formatted(certificateName);

            Gauge.builder(nameMetric, () -> daysRemaining)
                    .description("certificate expiration metric %s".formatted(certificateName))
                    .baseUnit("days")
                    .tag("certificate_name", certificateName)
                    .register(registry);

        }
    }

    private void initKeyStore() {
        String keystorePath = "keystore.jks";
        String keystorePassword = "changeit";
        String keystoreType = "JKS";

        try (FileInputStream fis = new FileInputStream(keystorePath);) {
            keyStore = KeyStore.getInstance(keystoreType);
            keyStore.load(fis, keystorePassword.toCharArray());
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private long getDifferentDaysWithDateNow(LocalDate date) {
        LocalDate dateNow = LocalDate.now();
        return ChronoUnit.DAYS.between(dateNow, date);
    }

}
