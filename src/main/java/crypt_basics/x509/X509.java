package crypt_basics.x509;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.logging.Logger;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class X509 {

	/**
	 * Logger for this class
	 */
	Logger logger = Logger.getLogger(X509.class.getName());

	/**
	 * The key pair for the certificate
	 */
	private KeyPair keyPair;

	/**
	 * Constructor for the X509 class, Generates a key pair.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public X509() throws NoSuchAlgorithmException, NoSuchProviderException {

		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
		keyPairGenerator.initialize(2048, new SecureRandom());

		keyPair = keyPairGenerator.generateKeyPair();

	}

	/**
	 * This method generates a self-signed X509 certificate
	 * 
	 * inspired by:
	 * https://github.com/bcgit/bc-java/blob/1c34996c9b89e87f8755e9a07a3addbdd9b2ce86/misc/src/main/java/org/bouncycastle/jcajce/examples/AttrCertExample.java#L28
	 * 
	 * @return the generated certificate
	 * @throws OperatorCreationException
	 * @throws CertificateException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 */
	public X509Certificate generateX509() throws OperatorCreationException, CertificateException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		X500Name issuer = new X500Name("CN=Test");
		BigInteger serialNumber = BigInteger.valueOf(1);
		Date startDate = Date.from(Instant.now());
		Date endDate = Date.from(ZonedDateTime.now().plusYears(2).toInstant());

		X500Name subject = new X500Name("C=AT, O=TestCert, OU=TestCert");

		X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issuer, serialNumber, startDate, endDate,
				subject, keyPair.getPublic());

		X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(builder
				.build(new JcaContentSignerBuilder("SHA1WithRSA").setProvider("BC").build(keyPair.getPrivate())));

		cert.checkValidity(new Date());

		cert.verify(keyPair.getPublic());

		return cert;
	}

	/**
	 * This method reads the certificate and prints out some information
	 * 
	 * @param cert the certificate to read
	 */
	public void readCert(X509Certificate cert) {
		logger.info(cert.getSigAlgName());
		logger.info(cert.getIssuerX500Principal().getName());
		logger.info(cert.getSubjectX500Principal().getName());
		logger.info(cert.getNotAfter().toString());
		logger.info(cert.getNotBefore().toString());
		logger.info(cert.getSerialNumber().toString());
	}

	/**
	 * This method verifies the certificate
	 * 
	 * @param cert the certificate to verify
	 * @throws InvalidKeyException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 */
	public void verifCert(X509Certificate cert) throws InvalidKeyException, CertificateException,
			NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		cert.checkValidity(new Date());
		cert.verify(keyPair.getPublic());
	}
}
