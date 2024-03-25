# Crypt Basics

This repo includes the basic example of the following implementations:
 * RSA encryption and decryption with key generation
 * scalar multiplication on elliptic curves
 * DSA signature generation anv verification
 * A basic networking example
 * x509 certificate generation and verification
 * HMAC generation and verification
 * Simple hashing
 * symmetric encryption with AES

## How to build

This repo was tested with Apache Maven 3.6.3 on (K)Ubuntu 22.04.4 LTS.

For build or test use

```sh
mvn build
mvn test
```

## Structure

The source code follows the standart Maven structure for code.

A basic latex documentation can be found in `./doc/`.