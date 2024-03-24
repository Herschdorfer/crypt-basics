package crypt_basics.hash;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HexFormat;

import org.junit.jupiter.api.Test;

class HashTest {

	@Test
	void test128() {
		assertDoesNotThrow(() -> {
			Hash sut = new Hash(128);
			String expected = "5f4dcc3b5aa765d61d8327deb882cf99";
			assertEquals(expected, HexFormat.of().formatHex(sut.hash("password".getBytes("UTF-8"))));
		});
	}

	@Test
	void test160() {
		assertDoesNotThrow(() -> {
			Hash sut = new Hash(160);
			String expected = "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";
			assertEquals(expected, HexFormat.of().formatHex(sut.hash("password".getBytes("UTF-8"))));
		});
	}

	@Test
	void test256() {
		assertDoesNotThrow(() -> {
			Hash sut = new Hash(256);
			String expected = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
			assertEquals(expected, HexFormat.of().formatHex(sut.hash("password".getBytes("UTF-8"))));
		});
	}

	@Test
	void test384() {
		assertDoesNotThrow(() -> {
			Hash sut = new Hash(384);
			String expected = "a8b64babd0aca91a59bdbb7761b421d4f2bb38280d3a75ba0f21f2bebc45583d446c598660c94ce680c47d19c30783a7";
			assertEquals(expected, HexFormat.of().formatHex(sut.hash("password".getBytes("UTF-8"))));
		});
	}

	@Test
	void test512() {
		assertDoesNotThrow(() -> {
			Hash sut = new Hash(512);
			String expected = "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86";
			assertEquals(expected, HexFormat.of().formatHex(sut.hash("password".getBytes("UTF-8"))));
		});
	}
}
