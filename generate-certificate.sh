#!/bin/bash

CERT_ALIAS="my-service-cert"
CERT_ALIAS2="s0me-serv-cert"
CERT_VALIDITY_DAYS=365
KEYSTORE_PASSWORD="changeit"
KEY_PASSWORD="changeit"
KEYSTORE_FILENAME="keystore.jks"

keytool -genkeypair -alias $CERT_ALIAS -keyalg RSA -keysize 2048 -validity $CERT_VALIDITY_DAYS -keystore $KEYSTORE_FILENAME -storepass $KEYSTORE_PASSWORD -keypass $KEY_PASSWORD -dname "CN=Iam,OU=Digital Habits,O=DigitalHabits,C=RU"

keytool -genkeypair -alias $CERT_ALIAS2 -keyalg RSA -keysize 2048 -validity $CERT_VALIDITY_DAYS -keystore $KEYSTORE_FILENAME -storepass $KEYSTORE_PASSWORD -keypass $KEY_PASSWORD -dname "CN=Iam,OU=Digital Habits,O=DigitalHabits,C=RU"

echo "Сертификат успешно сгенерирован"