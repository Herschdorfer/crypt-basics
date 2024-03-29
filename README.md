# Crypt Basics

This repo includes the basic example of the following implementations:
 * RSA encryption and decryption with key generation
 * scalar multiplication on elliptic curves
 * DSA signature generation anv verification
 * a basic networking example
 * x509 certificate generation and verification
 * HMAC generation and verification
 * simple hashing
 * symmetric encryption with AES

## How to build

This repo was tested with Apache Maven 3.6.3 on (K)Ubuntu 22.04.4 LTS.

For build or test use

```sh
mvn build
mvn test
```

## Structure

The source code follows the standard Maven structure for code.

`src/main/java/*` contains the implementation.
`src/main/test/*` contains the test code.

A basic latex documentation can be found in `./doc/`.

## Coverage and Linting

This project is tested and linted by a GitHub Actions and [SonarCloud](https://sonarcloud.io/project/overview?id=Herschdorfer_crypt-basics).