package uk.co.magictractor.zillions.testbed.importer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import uk.co.magictractor.zillions.core.BigInt;
import uk.co.magictractor.zillions.core.BigIntFactory;
import uk.co.magictractor.zillions.core.environment.Environment;
import uk.co.magictractor.zillions.core.importer.ByteImporter;

public abstract class ByteImporterTest {

	@Test
	public void testZero() {
		BigInt v = from(0x00);
		assertThat(v.toString()).isEqualTo("0");
	}

	@Test
	public void testSingleSetBit() {
		BigInt v = from(0x40, 0x00, 0x00, 0x00);
		// 2^30
		assertThat(v.toString()).isEqualTo("1073741824");
	}

	@Test
	public void testOne() {
		BigInt v = from(0x01);
		assertThat(v.toString()).isEqualTo("1");
	}

	@Test
	public void testMinusOneSingleByte() {
		BigInt v = from(0xff);
		assertThat(v.toString()).isEqualTo("-1");
	}
	
	@Test
	public void testMinusOneMultipleBytes() {
		BigInt v = from(0xff, 0xff, 0xff);
		assertThat(v.toString()).isEqualTo("-1");
	}
	
	@Test
	public void testMinusOneFourBytes() {
		BigInt v = from(0xff, 0xff, 0xff, 0xff);
		assertThat(v.toString()).isEqualTo("-1");
	}
	
	@Test
	public void testNegativeByte() {
		BigInt v = from(0x88);
		assertThat(v.toString()).isEqualTo("-120");
	}

	private BigInt from(int... bytesAsInts) {
		byte[] bytes = new byte[bytesAsInts.length];
		for (int i = 0; i < bytes.length; i++) {
			int byteAsInt = bytesAsInts[i];
			if ((byteAsInt & 0xffffff00) > 0) {
				throw new IllegalArgumentException();
			}
			bytes[i] = (byte) byteAsInt;
		}

		// StrategiesUtil.dumpStrategyInfo(Importer.class);

		ByteImporter importer = Environment.getBestAvailableImplementation(ByteImporter.class);
		BigInt rop = BigIntFactory.from(999);
		return importer.from(rop, bytes);
	}

}
