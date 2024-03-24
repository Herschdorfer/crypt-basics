package crypt_basics.x509;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class X509Test {

	@BeforeAll
	static void setUp() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Test
	void testX509Generation() {
		assertDoesNotThrow(() -> {
			X509 x509 = new X509();

			X509Certificate cert = x509.generateX509();

			cert.checkValidity();

			assertEquals("SHA1WITHRSA", cert.getSigAlgName());
			assertEquals("CN=Test", cert.getIssuerX500Principal().getName());
			assertEquals("OU=TestCert,O=TestCert,C=AT", cert.getSubjectX500Principal().getName());
			assertTrue(cert.getNotAfter().after(cert.getNotBefore()));
			assertEquals(java.math.BigInteger.valueOf(1), cert.getSerialNumber());

			x509.verifyCert(cert);

			x509.readCert(cert);

		});

	}

}
